package fr.csmetz;

import java.awt.Color;
import java.awt.Graphics;

import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.util.GraphicsUtil;
import static com.cburch.logisim.std.Strings.S;


public class UAL extends InstanceFactory {


	private static final BitWidth WIDTH_DEFAULT = BitWidth.create(16);
	private static final String[] opNames = { "(A)", "(B)", "(A&B)", "(A|B)", "(!A)", "(!B)", "(A+B)", "(A-B)", "(A+1)", "(A-1)", "A * B", "(A >> 1)", "nop", "nop", "nop", "nop"};
	private static final boolean[] needsA = { true,  false,    true,    true,   true,  false,    true,    true,    true,    true,    true,       true, false, false, false, false};
	private static final boolean[] needsB = {false,   true,    true,    true,  false,   true,    true,    true,   false,   false,    true,      false, false, false, false, false};
	private static final int nb_valid_ops = 11;
	private static final String[] pinNames = { "A   ", "   B", " UAL", "   S", "Z ", "C ", "V " };
	
	private static final Direction[] pinNameDirs = {
		Direction.SOUTH, Direction.SOUTH, Direction.EAST, 
		Direction.NORTH, Direction.WEST, Direction.WEST, Direction.WEST };

  private static final int[] iconX = { 3, 10, 12, 14, 21, 17, 7 };
  private static final int[] iconY = { 2, 2, 8, 2, 2, 15, 15 };
  private static final int[] symbolX = { -140, -90, -70, -50, 0, -50, -90 };
  private static final int[] symbolY = { -20, -20, 0, -20, -20, 30, 30 };


  private static final int inputA = 0;
  private static final int inputB = 1;
  private static final int inputUAL = 2;

  private static final int outputS = 3;
  private static final int outputZ = 4;
  private static final int outputC = 5;
  private static final int outputV = 6;

	public UAL()
	{
		super("UAL");

		setAttributes(new Attribute[] { StdAttr.WIDTH }, 
				new Object[] { WIDTH_DEFAULT });
		
		setOffsetBounds(Bounds.create(-140, -20, 140, 50));
	
		final var ps = new Port[7];

		ps[inputA] = new Port(-110, -20, Port.INPUT, StdAttr.WIDTH);
		ps[inputA].setToolTip(S.fixedString("Input A"));

		ps[inputB] = new Port(-30, -20, Port.INPUT, StdAttr.WIDTH);
		ps[inputB].setToolTip(S.fixedString("Input B"));

		ps[inputUAL] = new Port(-120, 0, Port.INPUT, 4);
		ps[inputUAL].setToolTip(S.fixedString("Code UAL"));

		ps[outputS] = new Port(-70, 30, Port.OUTPUT, StdAttr.WIDTH);
		ps[outputS].setToolTip(S.fixedString("Output"));

		ps[outputZ] = new Port(-20, 0, Port.OUTPUT, 1);
		ps[outputC] = new Port(-30, 10, Port.OUTPUT, 1);
		ps[outputV] = new Port(-40, 20, Port.OUTPUT, 1);

		setPorts(ps);

	}

	public void propagate(InstanceState state)
	{
		Value inA = state.getPortValue(inputA);
		Value inB = state.getPortValue(inputB);
		Value inUAL = state.getPortValue(inputUAL);
		Value outS;
		Value outV;
		Value outC;
		Value outZ;
		if (inUAL.isFullyDefined())
		{
			BitWidth width = inA.getBitWidth();
			int wdInt = width.getWidth();

			int command = Math.toIntExact(inUAL.toLongValue());

			if (((needsA[command]) && (!inA.isFullyDefined())) || ((needsB[command]) && (!inB.isFullyDefined())))
			{
				if (((needsA[command]) && (inA.isErrorValue())) || ((needsB[command]) && (inB.isErrorValue())))
				{
					outS = Value.createError(width);
					outZ = outC = outV = Value.ERROR;
				}
				else
				{
					outS = Value.createUnknown(width);
					outZ = outC = outV = Value.UNKNOWN;
				}
			}
			else
			{
				// We can compute the outputs as the inputs are valid
				int VAL_A = Math.toIntExact(inA.toLongValue());
				int VAL_B = Math.toIntExact(inB.toLongValue());
				int N_VAL_A = Math.toIntExact(inA.not().toLongValue());
				int N_VAL_B = Math.toIntExact(inB.not().toLongValue());
				int out;
				switch (command)
				{
				case 0: 
					out = VAL_A; break;
				case 1: 
					out = VAL_B; break;
				case 2:
					out = VAL_A & VAL_B; break;
				case 3:
					out = VAL_A | VAL_B; break;
				case 4:
					out = N_VAL_A; break;
				case 5:
					out = N_VAL_B; break;
				case 6:
					out = VAL_A + VAL_B; break;
				case 7:
					out = VAL_A - VAL_B; break;
				case 8:
					out = VAL_A + 1; break;
				case 9:
					out = VAL_A - 1; break;
				case 10:
					out = VAL_A * VAL_B; break;
				case 11:
					out = VAL_A >> 1; break;
					
				default: 
					out = 0;
				}
				if(command <= nb_valid_ops) {
				outS = Value.createKnown(width, out);
				outZ = outS.toLongValue() == 0 ? Value.TRUE : Value.FALSE;
				outC = (out >> wdInt & 0x1) == 1 ? Value.TRUE : Value.FALSE;
				outV = (inA.get(wdInt - 1) == inA.get(wdInt - 1)) && (outS.get(wdInt - 1) != inA.get(wdInt - 1)) ? 
						Value.TRUE : Value.FALSE;
				}
				else {
					outS = Value.createError(width);
					outZ = outC = outV = Value.ERROR;
				}			
			}
		}
		else
		{
			if ((inUAL.isErrorValue()))
			{
				outS = Value.createError(inA.getBitWidth());
				outZ = outC = outV = Value.ERROR;
			}
			else
			{
				outS = Value.createUnknown(inA.getBitWidth());
				outZ = outC = outV = Value.createUnknown(inA.getBitWidth());
			}
		}
		state.setPort(outputS, outS, 1);
		state.setPort(outputZ, outZ, 1);
		state.setPort(outputC, outC, 1);
		state.setPort(outputV, outV, 1);
	}

	public void paintInstance(InstancePainter painter)
	{
	    for (int i = 0; i < pinNames.length; i++) {
	        painter.drawPort(i, pinNames[i], pinNameDirs[i]);
	      }
	      Graphics g = painter.getGraphics();
	      Bounds bds = painter.getBounds();
	      int x = bds.getX();
	      int y = bds.getY();
	      g.translate(x + 140, y + 20);
	      GraphicsUtil.switchToWidth(g, 2);
	      g.drawPolygon(symbolX, symbolY, symbolX.length);
	      
	      Value inUAL = painter.getPortValue(inputUAL);
	      if ((painter.getShowState()) && (inUAL.isFullyDefined()))
	      {
	        int command = Math.toIntExact(inUAL.toLongValue());
	        
	        GraphicsUtil.drawCenteredText(g, opNames[command], 
	          bds.getWidth() / 2 - 140, bds.getHeight() / 2 - 20);
	      }
	}

	public void paintIcon(InstancePainter painter)
	{
		Graphics g = painter.getGraphics();

		g.setColor(Color.BLACK);
		g.drawPolygon(iconX, iconY, iconX.length);
	}

	public void paintGhost(InstancePainter painter)
	{
	    Graphics g = painter.getGraphics();
	    GraphicsUtil.switchToWidth(g, 2);
	    g.drawPolygon(symbolX, symbolY, symbolX.length);
	}

}






