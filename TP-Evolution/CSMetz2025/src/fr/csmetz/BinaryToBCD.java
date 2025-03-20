package fr.csmetz;


import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.Instance;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.instance.StdAttr;

import java.lang.Math;

public class BinaryToBCD  extends InstanceFactory{

	private final static int height = 120;
	private final static int width = 60;
	
	public BinaryToBCD() {
		super("BinaryToBCD");	

		int default_bits = 16 ; 
		int nbOutputs = (int)(Math.ceil(Math.log(Math.pow(2, default_bits))/Math.log(10)));
		
		setAttributes(new Attribute[] { StdAttr.WIDTH }, new Object[] { BitWidth.create(default_bits) });

		setOffsetBounds(Bounds.create(-width, -height/2, width, height));

		Port[] ports = new Port[1 + nbOutputs];
		ports[0] = new Port(-width, 0, Port.INPUT, StdAttr.WIDTH); // Input
		for(int i = 0 ; i < nbOutputs ; ++i) {
			ports[i+1] = new Port(0, height/2 - (i+1) * 10, Port.OUTPUT, 4);
		}
	}

	protected void updatePorts(Instance instance) {

		int widthInput = instance.getAttributeValue(StdAttr.WIDTH).getWidth();
		int nbOutputs = (int)(Math.ceil(Math.log(Math.pow(2, widthInput))/Math.log(10))); 

		Port[] ports = new Port[1 + nbOutputs];
		ports[0] = new Port(-width, 0, Port.INPUT, StdAttr.WIDTH); // Input
		for(int i = 0 ; i < nbOutputs ; ++i) 
			ports[i+1] = new Port(0, height/2 - (i+1) * 10, Port.OUTPUT, 4);
		
		instance.setPorts(ports);
	}

	protected void configureNewInstance(Instance instance)
	{
		instance.addAttributeListener();
		updatePorts(instance);
	}

	protected void instanceAttributeChanged(Instance instance, Attribute<?> attr) {	
		if (attr == StdAttr.WIDTH) {
			instance.recomputeBounds();
			updatePorts(instance);
		} 
	}


	public void propagate(InstanceState state) {
		Value in = state.getPortValue(0);
		long inV = in.toLongValue();
		
		int nbOutputs = state.getInstance().getPorts().size() - 1;
		for(int i = 0 ; i < nbOutputs ; ++i) {
			Value out = Value.createKnown(BitWidth.create(4), inV % 10);
			state.setPort(i+1, out, out.getWidth() + 1);
			inV = inV / 10;
		}		
	}

	public void paintInstance(InstancePainter painter) {
		painter.drawRectangle(painter.getBounds(), "ToBCD");
		painter.drawPorts();
	}

}

