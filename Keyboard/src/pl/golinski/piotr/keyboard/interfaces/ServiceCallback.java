package pl.golinski.piotr.keyboard.interfaces;

public interface ServiceCallback<ResultT> {
	void onSuccess(ResultT result);

	void onError(Exception exception);

	void onNoResult();
}
