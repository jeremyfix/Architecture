package fr.csmetz;

import java.util.Arrays;
import java.util.List;

import com.cburch.logisim.tools.AddTool;
import com.cburch.logisim.tools.Library;
import com.cburch.logisim.tools.Tool;

import fr.csmetz.UAL;


public class CSMetz  extends Library {

	private List<AddTool> tools;
	
    public CSMetz() {
        tools = Arrays.asList(new AddTool[] {
                new AddTool(new UAL()),
                new AddTool(new BinaryToBCD())
        });
    }
    
    public String getDisplayName() {
        return "CSMetz";
    }	
	
	@Override
	public List<? extends Tool> getTools() {
		return tools;
	}

}
