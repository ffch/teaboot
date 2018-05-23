package com.teaboot.web.session;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class StandardSession implements HttpSession {
	protected ConcurrentMap<String, Object> attributes = new ConcurrentHashMap<>();
	protected long creationTime = 0L;
	protected transient volatile boolean expiring = false;
	protected String id = null;
	protected volatile long lastAccessedTime = creationTime;
	protected volatile boolean isNew = false;
	protected volatile boolean isValid = false;
	protected volatile int maxInactiveInterval = -1;
	protected static final String EMPTY_ARRAY[] = new String[0];
	protected SessionManager manager = null;
	
	public StandardSession(SessionManager manager) {
		super();
		this.manager = manager;
	}
	public StandardSession() {
		super();
	}

	@Override
	public long getCreationTime() {
		return creationTime;
	}

	public boolean isExpiring() {
		return expiring;
	}

	public void setExpiring(boolean expiring) {
		this.expiring = expiring;
	}

	public boolean isValidInternal() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLastAccessedTime(long lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}
	@Override
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public long getLastAccessedTime() {
		return lastAccessedTime;
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
		this.maxInactiveInterval = interval;
	}

	@Override
	public int getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	@Override
	public Object getAttribute(String name) {
		if (!isValidInternal())
			throw new IllegalStateException("standardSession.getAttribute.ise");

		if (name == null)
			return null;

		return (attributes.get(name));
	}

	@Override
	public Object getValue(String name) {
		return (getAttribute(name));
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		Set<String> names = new HashSet<>();
		names.addAll(attributes.keySet());
		return Collections.enumeration(names);
	}

	@Override
	public String[] getValueNames() {
		if (!isValidInternal())
			throw new IllegalStateException("standardSession.getValueNames.ise");

		return (keys());
	}

	protected String[] keys() {

		return attributes.keySet().toArray(EMPTY_ARRAY);

	}

	@Override
	public void setAttribute(String name, Object value) {
		attributes.put(name, value);
	}

	@Override
	public void putValue(String name, Object value) {
		setAttribute(name, value);
	}

	@Override
	public void removeAttribute(String name) {
		attributes.remove(name);
	}

	@Override
	public void removeValue(String name) {
		removeAttribute(name);
	}

	@Override
	public void invalidate() {
		if (!isValid)
			return;
		synchronized (this) {

			if (expiring || !isValid)
				return;
			expiring = true;
		}

	}

	@Override
	public boolean isNew() {
		return (this.isNew);
	}

}
