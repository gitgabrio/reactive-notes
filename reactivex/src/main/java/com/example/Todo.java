package com.example;

/**
 * @author gabriele.cardosi@horsa.it on 12/06/17.
 */
public class Todo
{

	private int id;

	public Todo(int id)
	{
		this.id = id;
	}

	@Override public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Todo todo = (Todo) o;

		return id == todo.id;

	}

	@Override public int hashCode()
	{
		return id;
	}

	@Override public String toString()
	{
		return "Todo{" +
		"id=" + id +
		'}';
	}
}
