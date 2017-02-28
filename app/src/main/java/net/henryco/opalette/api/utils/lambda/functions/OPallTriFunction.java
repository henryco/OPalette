package net.henryco.opalette.api.utils.lambda.functions;

/**
 * Created by HenryCo on 28/02/17.
 */
@FunctionalInterface
public interface OPallTriFunction <R, T1, T2, T3> {
	R apply(T1 t1, T2 t2, T3 t3);
}
