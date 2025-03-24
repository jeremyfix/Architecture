package fr.cs2015;

import java.util.Arrays;
import java.util.List;

import com.cburch.logisim.tools.AddTool;
import com.cburch.logisim.tools.Library;
import com.cburch.logisim.tools.Tool;

import fr.cs2015.UAL;


public class CSMetz2015  extends Library {

	private List<AddTool> tools;
	
    public CSMetz2015() {
        tools = Arrays.asList(new AddTool[] {
                new AddTool(new UAL()),
                new AddTool(new BinaryToBCD())
        });
    }
    
    public String getDisplayName() {
        return "CS2015";
    }	
	
	@Override
	public List<? extends Tool> getTools() {
		return tools;
	}

}
