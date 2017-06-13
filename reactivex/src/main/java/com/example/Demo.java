package com.example;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

import java.util.List;

/**
 * @author gabriele.cardosi@horsa.it on 12/06/17.
 */
public class Demo
{

	public static void main(String[] args)
	{
		Observable todoObservable = getObservable();
		Disposable disposable = todoObservable.subscribe(t -> System.out.println("On subscriber " + t));
		// Dispose the subscription when not interested in the emitted data any more
		disposable.dispose();
		// Also handle the error case with a second io.reactivex.functions.Consumer<T>
		//		Disposable subscribe = todoObservable.subscribe(t -> System.out.print(t),
		//		((Throwable) throwable) ->  throwable.printStackTrace());
	}

	private static Observable getObservable()
	{
		Observable<Todo> todoObservable = Observable.create(new ObservableOnSubscribe<Todo>()
		{
			@Override public void subscribe(ObservableEmitter<Todo> emitter) throws Exception
			{
				try
				{
					List<Todo> todos = new RxJavaUnitTest().getTodos();
					for (Todo todo : todos)
					{
						System.out.println("Sending " + todo + " to emitter");
						emitter.onNext(todo);
					}
					emitter.onComplete();
				}
				catch (Exception e)
				{
					emitter.onError(e);
				}
			}
		});
		return todoObservable;
	}
}
