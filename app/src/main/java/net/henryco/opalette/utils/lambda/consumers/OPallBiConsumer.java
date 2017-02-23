package net.henryco.opalette.utils.lambda.consumers;

/**
 * Created by HenryCo on 23/02/17.
 */
@FunctionalInterface
public interface OPallBiConsumer <T, U> {
	void consume(T t, U u);
}
