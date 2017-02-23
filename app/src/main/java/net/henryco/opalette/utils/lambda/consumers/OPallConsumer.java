package net.henryco.opalette.utils.lambda.consumers;

/**
 * Created by HenryCo on 23/02/17.
 */
@FunctionalInterface
public interface OPallConsumer <T> {
	void consume(T t);
}
