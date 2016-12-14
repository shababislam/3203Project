import java.util.Observable;

public class ObservableObject {
	
	public Observable parent;
	public Object object;
	public String message;
	
	private ObservableObject()
	{}
	public ObservableObject(Observable parent, Object object, String message)
	{
		this();
		this.parent = parent;
		this.object = object;
		this.message = message;
	}  
}
