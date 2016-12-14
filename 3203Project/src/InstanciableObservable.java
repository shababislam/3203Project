import java.util.Observable;
/**
 * this Class Allows other classes to instanciate an observerable object and gives access to their private methods
 * @author Ehsan Karami
 *
 */
public class InstanciableObservable extends Observable {
	
	public InstanciableObservable() 
	{
		super();
	}

	@Override
	public synchronized void clearChanged() {
		// TODO Auto-generated method stub
		super.clearChanged();
	}

	@Override
	public synchronized void setChanged() {
		// TODO Auto-generated method stub
		super.setChanged();
	}
	

}
