package fr.csmetz;

import static com.cburch.logisim.std.Strings.S;

import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.Attributes;
import com.cburch.logisim.data.AttributeSet;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.Instance;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.std.memory.ClockState;
import com.cburch.logisim.util.GraphicsUtil;

import java.awt.Color;

public class GraphicCard extends InstanceFactory {

	/*
	 * The GraphicCard component is a set of buffers, one 
	 * for each row of the screen. 
	 * This component uses double buffering to avoid flickering.
	 * You write to one buffer while the other one is sent to the output.
	 * Switching one buffer to another is done by writting on the I/O device
	 * adress.
	 * The address of the row's buffer is the base address plus the row number, 
	 * starting the row number at 1 (to account for the switch buffer)
	 * The switch buffer is nBits long although only the first bit is used to
	 * select the front/back buffer.
	 */

	// Number of bits per rows
	private final static int nBits = 16;	
	private final static int nRows = 16;
	private final static int width = 80;

	private static final Attribute<Integer> NROWS_OPTION =
		Attributes.forIntegerRange("nRows", S.fixedString("nRowsAttr"), 1, 128);

	private static final int inputAdrBus = 0;
	private static final int inputDataBus = 1;
	private static final int inputClk = 2;
	private static final int inputWE = 3;
	private static final int inputReset = 4;
	private static final int inputIOAdrBus = 5; // The base address of the I/O device
	// The other ports are the output ports which will
	// be dynamically created

	private Value switchBuffer;
	private Value[] buffers;

	private ClockState clockState;

	public GraphicCard() {
		super("GraphicCard");

		setAttributes(
				new Attribute[] {
					StdAttr.WIDTH,
					StdAttr.EDGE_TRIGGER,
					NROWS_OPTION
				},
				new Object[] {
					BitWidth.create(nBits),
					StdAttr.TRIG_RISING,
					16
				}
				);
        // setOffsetBounds(Bounds.create(-width, -50, width, 50));

	}

	@Override
	protected void configureNewInstance(Instance instance) {
		clockState = new ClockState();
		updatePorts(instance);
		updateBuffers(instance);
		instance.addAttributeListener();
	}

	@Override
	protected void instanceAttributeChanged(Instance instance, Attribute<?> attr) {

		if (attr == NROWS_OPTION) {
			int nRows = instance.getAttributeValue(NROWS_OPTION);
			instance.recomputeBounds();
			updatePorts(instance);
			updateBuffers(instance);
		}

	}

	@Override
	public Bounds getOffsetBounds(AttributeSet attrs) {
		int nRows = attrs.getValue(NROWS_OPTION);
		final var height = Math.max(10*nRows, 50)+20;
		return Bounds.create(-width, 0, width, height);
	}

	@Override
	public void propagate(InstanceState state) {

		// If reset is true, reset all buffers
		if(state.getPortValue(inputReset) == Value.TRUE) {
			for(int i = 0; i < 2*nRows; i++) {
				buffers[i] = Value.createKnown(BitWidth.create(nBits), 0);
			}
		}

		// Determine if the clock triggers an update
		Object triggerType = state.getAttributeValue(StdAttr.EDGE_TRIGGER);
		boolean triggered = clockState.updateClock(state.getPortValue(inputClk), triggerType);


		// The Value of the buffer, selected from the AddrBus is updated
		// if Write Enable is True and the clock is rising edge
		// Depending on the value of the switch buffer, it is either the first
		// set of buffers that is updated or the second set
		//
		long addrValue = state.getPortValue(inputAdrBus).toLongValue();
		long ioAddrValue = state.getPortValue(inputIOAdrBus).toLongValue();
		if(addrValue < ioAddrValue || addrValue >= ioAddrValue + nRows) {
			return;
		}

		int bufferIdx = Math.toIntExact(addrValue - ioAddrValue - 1);
		int switchBufferIdx = Math.toIntExact(switchBuffer.get(0).toLongValue());

		if(state.getPortValue(inputWE) == Value.TRUE && triggered) {
			buffers[switchBufferIdx * nRows + bufferIdx] = state.getPortValue(inputDataBus);
		}

		// Update the output depending on the switch buffer
		for(int i = 0; i < nRows; i++) {
			state.setPort(i, buffers[(1 - switchBufferIdx)*nRows + i], nBits);
		}
	}


	private void updatePorts(Instance instance) {
		int nRows = instance.getAttributeValue(NROWS_OPTION);
		
		final var ports = new Port[nRows+inputIOAdrBus+1];

		// First define the output ports
		for(int i = 0; i < nRows; i++) {
			ports[i] = new Port(0, 10*(i+1), Port.OUTPUT, StdAttr.WIDTH); // Output
		}

		final var basePortIdx = nRows;

		ports[basePortIdx + inputAdrBus] = new Port(-width, 10, Port.INPUT, StdAttr.WIDTH); 
		ports[basePortIdx + inputAdrBus].setToolTip(S.fixedString("AddrBus"));

		ports[basePortIdx + inputDataBus] = new Port(-width, 20, Port.INPUT, StdAttr.WIDTH); 
		ports[basePortIdx + inputDataBus].setToolTip(S.fixedString("DataBus"));

		ports[basePortIdx + inputClk] = new Port(-width, 30, Port.INPUT, 1); 
		ports[basePortIdx + inputClk].setToolTip(S.fixedString("Clock"));

		ports[basePortIdx + inputWE] = new Port(-width, 40, Port.INPUT, 1); 
		ports[basePortIdx + inputWE].setToolTip(S.fixedString("Write Enable"));

		ports[basePortIdx + inputReset] = new Port(-width, 50, Port.INPUT, 1);
		ports[basePortIdx + inputReset].setToolTip(S.fixedString("Reset"));

		ports[basePortIdx + inputIOAdrBus] = new Port(-width, 60, Port.INPUT, StdAttr.WIDTH);
		ports[basePortIdx + inputIOAdrBus].setToolTip(S.fixedString("IO Base Address"));

		instance.setPorts(ports);
	}

	private void updateBuffers(Instance instance) {
		switchBuffer = Value.createKnown(BitWidth.create(nBits), 0);

		buffers = new Value[2*nRows];
		for(int i = 0; i < 2*nRows; i++) {
			buffers[i] = Value.createKnown(BitWidth.create(nBits), 0);
		}
	}

	@Override
	public void paintInstance(InstancePainter painter) {

		int nRows = painter.getAttributeValue(NROWS_OPTION);
		final var basePortIdx = nRows;

		// draw the rectangle
		painter.drawRectangle(painter.getBounds(), "GraphicCard");
		final var g = painter.getGraphics();
		// g.drawRoundRect(0, 0, width, 10*painter.getAttributeValue(NROWS_OPTION), 3, 3);
		painter.drawClock(nRows + inputClk, Direction.EAST);
		// draw the text
		// painter.drawLabel("GraphicCard", "center");
		painter.drawPorts();

		String text_first = buffers[0].toBinaryString();
		GraphicsUtil.drawCenteredText(g, text_first, 0, 0);
	}

}



