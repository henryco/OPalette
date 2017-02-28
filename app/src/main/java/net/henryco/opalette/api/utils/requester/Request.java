package net.henryco.opalette.api.utils.requester;

import net.henryco.opalette.api.utils.lambda.consumers.OPallConsumer;

/**
 * Created by HenryCo on 27/02/17.
 */

public class Request {

	public static final class Destination {
		private long[] dest;
		private long[] excp;

		private Destination() {
			all();
		}
		public Destination id(long ... id) {
			dest = new long[id.length];
			System.arraycopy(id, 0, dest, 0, id.length);
			return this;
		}
		public Destination all() {
			dest = new long[0];
			excp = new long[0];
			return this;
		}
		public Destination except(long ... id) {
			excp = new long[id.length];
			System.arraycopy(id, 0, excp, 0, id.length);
			return this;
		}
		public long[] getDestinations(){
			long[] dst = new long[dest.length];
			System.arraycopy(dest, 0, dst, 0, dst.length);
			return dst;
		}
		public long[] getExceptions(){
			long[] ecp = new long[excp.length];
			System.arraycopy(excp, 0, ecp, 0, ecp.length);
			return ecp;
		}

	}
	public final Destination destination = new Destination();
	public Request destination(OPallConsumer<Destination> d) {
		d.consume(destination);
		return this;
	}

	public interface requestor {
		void open(int req_id, String request, Object data);
	}

	public static final boolean AND = true;
	public static final boolean OR = false;


	private final Runnable requestAction;
	private final int requestID;
	private final String request;
	private final Object data;




	public Request(int requestID, String request, Runnable requestAction, Object ... data) {
		this.request = request;
		this.requestID = requestID;
		this.requestAction = requestAction;
		if (data.length == 0) this.data = null;
		else if (data.length == 1) this.data = data[0];
		else this.data = data;
	}
	public Request(int requestID, String request, Object ... data) {
		this(requestID, request, null, data);
	}
	public Request(int requestID, Object ... data) {
		this(requestID, "NULL", null, data);
	}
	public Request(String request, Object ... data) {
		this(-1, request, null, data);
	}
	public Request(int requestID, Runnable requestAction, Object ... data) {
		this(requestID, "NULL", requestAction, data);
	}
	public Request(String request, Runnable requestAction, Object ... data) {
		this(-1, request, requestAction, data);
	}


	public void openRequest(Runnable r) {
		r.run();
		closeRequest();
	}

	public void openRequest(int requestID, Runnable r) {
		if (requestID == this.requestID) openRequest(r);
	}

	public void openRequest(String request, Runnable r) {
		if (request.equalsIgnoreCase(this.request)) openRequest(r);
	}

	public void openRequest(int requestID, String request, Runnable r) {
		if (checkRequest(requestID, request, AND)) openRequest(r);
	}

	public void openRequest(requestor r) {
		r.open(requestID, request, data);
		closeRequest();
	}

	public void closeRequest() {
		if (requestAction != null) requestAction.run();
	}

	public boolean checkRequest(int requestID, String request, boolean and_or) {
		if (and_or == AND) return requestID == this.requestID && request.equalsIgnoreCase(this.request);
		return requestID == this.requestID || request.equalsIgnoreCase(this.request);
	}

	@SuppressWarnings("unchecked")
	public <T> T getData() {
		return (T) data;
	}
}
