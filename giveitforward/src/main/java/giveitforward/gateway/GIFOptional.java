package giveitforward.gateway;

import giveitforward.models.Model;


/**
 * Optional return type is useful when we want to return an errorMessage or an object. Important, you cannot have
 * both an errorMessage and an object, you may only have one or the other.
 */
public class GIFOptional {

	private String errorMessage = null;
	private Model object = null;

	public GIFOptional(String err){
		this.errorMessage = err;
	}

	public GIFOptional(Model obj){
		this.object = obj;
	}

	public GIFOptional(){}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.object = null;
		this.errorMessage = errorMessage;
	}

	public Model getObject() {
		return object;
	}

	public void setObject(Model object) {
		this.errorMessage = null;
		this.object = object;
	}
}
