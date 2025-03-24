package fr.cs2015;

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




public class UAL extends InstanceFactory {


	private static final BitWidth WIDTH_DEFAULT = BitWidth.create(16);
	private static final String[] opNames = { "(A)", "(B)", "(A&B)", "(A|B)", "(!A)", "(!B)", "(A+B)", "(A-B)", "(A+1)", "(A-1)", "A * B", "(A >> 1)", "nop", "nop", "nop", "nop"};
	private static final boolean[] needsA = { true,  false,    true,    true,   true,  false,    true,    true,    true,    true,    true,       true, false, false, false, false};
	private static final boolean[] needsB = {false,   true,    true,    true,  false,   true,    true,    true,   false,   false,    true,      false, false, false, false, false};
	private static final int nb_valid_ops = 11;
	private static final String[] pinNames = { "A   ", "   B", " U0", " U1", " U2", " U3", "   S", "Z ", "C ", "V " };
	
	private static final Direction[] pinNameDirs = {
		Direction.SOUTH, Direction.SOUTH, Direction.EAST, Direction.EAST, Direction.EAST, Direction.EAST, 
		Direction.NORTH, Direction.WEST, Direction.WEST, Direction.WEST };

  private static final int[] iconX = { 3, 10, 12, 14, 21, 17, 7 };
  private static final int[] iconY = { 2, 2, 8, 2, 2, 15, 15 };
  private static final int[] symbolX = { -140, -90, -70, -50, 0, -50, -90 };
  private static final int[] symbolY = { -20, -20, 0, -20, -20, 30, 30 };

	public UAL()
	{
		super("UAL");

		setAttributes(new Attribute[] { StdAttr.WIDTH }, 
				new Object[] { WIDTH_DEFAULT });
		
		setOffsetBounds(Bounds.create(-140, -20, 140, 50));
		
	    setPorts(new Port[] {
	    	      new Port(-110, -20, "input", StdAttr.WIDTH), 
	    	      new Port(-30, -20, "input", StdAttr.WIDTH), 
	    	      new Port(-130, -10, "input", 1), 
	    	      new Port(-120, 0, "input", 1), 
	    	      new Port(-110, 10, "input", 1), 
	    	      new Port(-100, 20, "input", 1), 
	    	      new Port(-70, 30, "output", StdAttr.WIDTH), 
	    	      new Port(-20, 0, "output", 1), 
	    	      new Port(-30, 10, "output", 1), 
	    	      new Port(-40, 20, "output", 1) });
	}

	public void propagate(InstanceState state)
	{
		Value inA = state.getPort(0);
		Value inB = state.getPort(1);
		Value inU0 = state.getPort(2);
		Value inU1 = state.getPort(3);
		Value inU2 = state.getPort(4);
		Value inU3 = state.getPort(5);
		Value outS;
		Value outV;
		Value outC;
		Value outZ;
		if ((inU0.isFullyDefined()) && (inU1.isFullyDefined()) && (inU2.isFullyDefined()) && (inU3.isFullyDefined()))
		{
			BitWidth width = inA.getBitWidth();
			int wdInt = width.getWidth();

			int BIT_U0 = inU0.toIntValue();
			int BIT_U1 = inU1.toIntValue();
			int BIT_U2 = inU2.toIntValue();
			int BIT_U3 = inU3.toIntValue();

			int command = 8 * BIT_U3 + 4 * BIT_U2 + 2 * BIT_U1 + BIT_U0;
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
				int VAL_A = inA.toIntValue();
				int VAL_B = inB.toIntValue();
				int N_VAL_A = inA.not().toIntValue();
				int N_VAL_B = inB.not().toIntValue();
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
				outZ = outS.toIntValue() == 0 ? Value.TRUE : Value.FALSE;
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
			if ((inU0.isErrorValue()) || (inU1.isErrorValue()) || (inU2.isErrorValue()) || (inU3.isErrorValue()))
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
		state.setPort(6, outS, 1);
		state.setPort(7, outZ, 1);
		state.setPort(8, outC, 1);
		state.setPort(9, outV, 1);
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
	      
	      Value inU0 = painter.getPort(2);
	      Value inU1 = painter.getPort(3);
	      Value inU2 = painter.getPort(4);
	      Value inU3 = painter.getPort(5);
	      if ((painter.getShowState()) && (inU0.isFullyDefined()) && (inU1.isFullyDefined()) && (inU2.isFullyDefined()) && (inU3.isFullyDefined()))
	      {
	        int BIT_U0 = inU0.toIntValue();
	        int BIT_U1 = inU1.toIntValue();
	        int BIT_U2 = inU2.toIntValue();
	        int BIT_U3 = inU3.toIntValue();
	        int command = 8 * BIT_U3 + 4 * BIT_U2 + 2 * BIT_U1 + BIT_U0;
	        
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






