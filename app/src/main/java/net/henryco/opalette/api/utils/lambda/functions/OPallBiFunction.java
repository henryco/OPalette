package net.henryco.opalette.api.utils.lambda.functions;

/**
 * Created by HenryCo on 23/02/17.
 */
@FunctionalInterface
public interface OPallBiFunction <R, T1, T2> {
	R apply(T1 t1, T2 t2);
}
