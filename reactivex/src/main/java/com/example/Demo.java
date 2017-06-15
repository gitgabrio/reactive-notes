package com.example;

//import io.reactivex.Observable;
//import io.reactivex.ObservableEmitter;
//import io.reactivex.ObservableOnSubscribe;
//import io.reactivex.disposables.Disposable;

import rx.*;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.observables.AsyncOnSubscribe;
import rx.observables.SyncOnSubscribe;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author gabriele.cardosi@horsa.it on 12/06/17.
 */
public class Demo
{

	public static void main(String[] args)
	{

		//		Observable todoObservable = getObservable();
		//		System.out.println("Subscribe to todoObservable");
		//		Subscription subscription = todoObservable.subscribe(t -> System.out.println("On subscriber " + t));
		//		System.out.println("subscription.unsubscribe()");
		//		subscription.unsubscribe();
		Single single = getSingle();
		System.out.println("Subscribe to safeObservable");
		Subscription subscription = single.subscribe(new Action1<List<Todo>>()
		{
			@Override public void call(List<Todo> todos)
			{
				todos.forEach(System.out::println);
			}
		});
		System.out.println("subscription.unsubscribe()");
		subscription.unsubscribe();
		//		Observer<Todo> observer = new Observer<Todo>()
		//		{
		//			@Override public void onCompleted()
		//			{
		//				System.out.println("Observer.onCompleted");
		//			}
		//
		//			@Override public void onError(Throwable throwable)
		//			{
		//				System.out.println("Observer.onError");
		//			}
		//
		//			@Override public void onNext(Todo todo)
		//			{
		//				System.out.println("Observer.onNext " + todo);
		//			}
		//		};

	}

	private static Single getSingle()
	{
		return Single.create(new Single.OnSubscribe<List<Todo>>()
		{
			@Override public void call(SingleSubscriber<? super List<Todo>> singleSubscriber)
			{
				ExampleCallback exampleCallback = new ExampleCallback()
				{
					@Override public void apply(List<Todo> todos)
					{
						singleSubscriber.onSuccess(todos);
						singleSubscriber.unsubscribe();
					}
				};
				CallbackExecutor callbackExecutor = new CallbackExecutor(exampleCallback);
				Thread thread = new Thread(callbackExecutor);
				thread.start();
			}
		});

//		return Observable.create(SyncOnSubscribe.createStateless(new Action1<Observer<? super Todo>>()
//		{
//			@Override public void call(Observer<? super Todo> observer)
//			{
//				ExampleCallback exampleCallback = new ExampleCallback()
//				{
//					@Override public void apply(List<Todo> todos)
//					{
//						for (Todo todo : todos)
//						{
//							System.out.println("Sending " + todo + " to Observer from exampleCallback");
//							observer.onNext(todo);
//						}
//						observer.onCompleted();
//						//												observer.
//					}
//				};
//				CallbackExecutor callbackExecutor = new CallbackExecutor(exampleCallback);
//				Thread thread = new Thread(callbackExecutor);
//				thread.start();
//			}
//		}));
		//		return Observable.create(new SyncOnSubscribe<Long, Todo>()
		//		{
		//			private volatile long state = 0;
		//			@Override protected Long generateState()
		//			{
		//				state ++;
		//				return new Long(state);
		//			}
		//
		//			@Override protected Long next(Long longVar, Observer<? super Todo> observer)
		//			{
		//				return null;
		//			}
		//		});
	}

	private static Observable getObservable()
	{
		//		new Observable<Todo>().

		Observable<Todo> toReturn = Observable.create(new Observable.OnSubscribe<Todo>()
		{

			@Override public void call(Subscriber<? super Todo> subscriber)
			{
				ExampleCallback exampleCallback = new ExampleCallback()
				{
					@Override public void apply(List<Todo> todos)
					{
						for (Todo todo : todos)
						{
							System.out.println("Sending " + todo + " to Subscriber from exampleCallback");
							subscriber.onNext(todo);
						}
						subscriber.onCompleted();
						//						subscriber.unsubscribe();
					}
				};
				CallbackExecutor callbackExecutor = new CallbackExecutor(exampleCallback);
				Thread thread = new Thread(callbackExecutor);
				thread.start();
				//				new Thread().run(new CallbackExecutor(exampleCallback) );
				//				CallbackExecutor callbackExecutor = new CallbackExecutor(exampleCallback);
				//				callbackExecutor.run();
				//				while (!subscriber.isUnsubscribed()) {
				//					System.out.println("Wait");
				//					try
				//					{
				//						Thread.sleep(1000);
				//					}
				//					catch (InterruptedException e)
				//					{
				//						e.printStackTrace();
				//					}
				//				}
				//				System.out.println("Stopping callbackExecutor");
				//				callbackExecutor.toStop = true;
			}
		});
		return toReturn;
	}

	private static class CallbackExecutor implements Runnable
	{
		private ExampleCallback exampleCallback;

		public CallbackExecutor(ExampleCallback exampleCallback)
		{
			this.exampleCallback = exampleCallback;
		}

		public void run()
		{
			System.out.println("CallbackExecutor.run");
			try
			{
				System.out.println("CallbackExecutor.sleep");
				for (int i = 0; i < 15; i++)
				{
					Thread.sleep(1000);
				}
				System.out.println("CallbackExecutor.exampleCallback.apply");
				exampleCallback.apply(new RxJavaUnitTest().getTodos());
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
