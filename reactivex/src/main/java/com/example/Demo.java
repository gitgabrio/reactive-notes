package com.example;

import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.functions.Func1;

import java.util.List;

/**
 * @author gabriele.cardosi@horsa.it on 12/06/17.
 */
public class Demo
{

	public static void main(String[] args)
	{

		Observable.range(1, 10).flatMap(v -> Observable.range(v, 2)).subscribe(System.out::println);
		Single<List<Todo>> singleList = getListSingle();
		singleList.subscribe(todos -> {
			System.out.printf("onSuccess ");
			todos.forEach(System.out::println);
		}, throwable -> {
			System.out.println("onError");
			throwable.printStackTrace();
		});
		//Single<Todo> singleItem = getChainedSingle(3);
		SingleSubscriber<Todo> singleSubscriber = new SingleSubscriber<Todo>()
		{
			@Override public void onSuccess(Todo todo)
			{
				System.out.printf("onSuccess ");
				System.out.println(todo);
			}

			@Override public void onError(Throwable throwable)
			{
				System.out.println("onError " + throwable.getMessage());
			}
		};
		getChainedSingle(3).subscribe(singleSubscriber);
		getChainedSingle(59).subscribe(singleSubscriber);
	}

	private static Single<List<Todo>> getListSingle()
	{
		Single<List<Todo>> toReturn = Single.create(new Single.OnSubscribe<List<Todo>>()
		{
			@Override public void call(SingleSubscriber<? super List<Todo>> singleSubscriber)
			{
				List<Todo> toReturn = new RxJavaUnitTest().getTodos();
				singleSubscriber.onSuccess(toReturn);
				//				singleSubscriber.onError();
			}
		});
		return toReturn;
	}

	private static Single<Todo> getChainedSingle(final int expectedTodoId)
	{
		Single<Todo> toReturn = getListSingle().flatMap(new Func1<List<Todo>, Single<Todo>>()
		{
			@Override public Single<Todo> call(List<Todo> iterable)
			{
				return Single.create(new Single.OnSubscribe<Todo>()
				{
					@Override public void call(SingleSubscriber<? super Todo> singleSubscriber)
					{
						Todo expected = new Todo(expectedTodoId);
						if (iterable.contains(expected))
						{
							singleSubscriber.onSuccess(expected);
						}
						else
						{
							singleSubscriber.onError(new Exception("Could not find " + expected));
						}
					}
				});
			}
		});
		return toReturn;
	}
}
