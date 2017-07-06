package org.springframework.boot.actuate.trace;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Created by wisebirds on 2017-07-06.
 */
public abstract class AbstractTraceRepository implements TraceRepository {

	private String pageKey = "page";
	private String sizeKey = "size";

	private static HttpServletRequest getRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes instanceof ServletRequestAttributes) {
			return ((ServletRequestAttributes) requestAttributes).getRequest();
		}
		return null;
	}

	@Override
	public List<Trace> findAll() {
		HttpServletRequest request = getRequest();
		return findAll(request, new PageRequest(nullSafeValue(request, getPageKey()), nullSafeValue(request, getSizeKey())));
	}

	@Override
	public void add(Map<String, Object> traceInfo) {
		add(new Trace(new Date(), traceInfo));
	}

	public abstract void add(Trace trace);

	public abstract List<Trace> findAll(HttpServletRequest request, Pageable pageable);

	private int nullSafeValue(HttpServletRequest request, String key) {
		if(request == null) {
			return 0;
		}
		String parameter = request.getParameter(key);
		if (parameter != null) {
			return Integer.valueOf(parameter);
		}
		return 0;
	}

	public String getPageKey() {
		return pageKey;
	}

	public void setPageKey(String pageKey) {
		this.pageKey = pageKey;
	}

	public String getSizeKey() {
		return sizeKey;
	}

	public void setSizeKey(String sizeKey) {
		this.sizeKey = sizeKey;
	}
}
