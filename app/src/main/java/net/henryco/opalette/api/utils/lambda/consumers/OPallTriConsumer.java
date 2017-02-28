package net.henryco.opalette.api.utils.lambda.consumers;

/**
 * Created by HenryCo on 28/02/17.
 */
@FunctionalInterface
public interface OPallTriConsumer<U, V, T> {
	void consume(U u, V v, T t);
}
