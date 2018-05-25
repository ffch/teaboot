package com.teaboot.web.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.teaboot.context.utils.StringUtil;

public class SessionManager {
	private Context context = null;
	StandardSessionIdGenerator standardSessionIdGenerator = new StandardSessionIdGenerator();
	protected Map<String, HttpSession> sessions = new ConcurrentHashMap<>();
	protected volatile int maxActive = 0;
	private int sessionCounter = 0;
	protected final AtomicLong expiredSessions = new AtomicLong(0);
	private static SessionManager manager = new SessionManager();

	public static SessionManager getInstance() {
		return manager;
	}

	public HttpSession getSession(String sessionId, boolean refresh) {
		if(StringUtil.isEmpty(sessionId)){
			return createSession(null);
		}
		if (sessions.get(sessionId) != null) {
			if (refresh) {
				HttpSession session = sessions.get(sessionId);
				String id = standardSessionIdGenerator.generateSessionId();
				session.setId(id);
				session.setNew(true);
				session.setValid(true);
				session.setCreationTime(System.currentTimeMillis());
				if (context != null)
					session.setMaxInactiveInterval(getContext().getSessionTimeout() * 60);
				sessions.put(id, session);
				return session;
			} else {
				return sessions.get(sessionId);
			}
		}

		HttpSession httpSession = createSession(sessionId);
		return httpSession;

	}

	public HttpSession createSession(String sessionId) {
		// Recycle or create a Session instance
		HttpSession session = createEmptySession();

		// Initialize the properties of the new session and return it
		session.setNew(true);
		session.setValid(true);
		session.setCreationTime(System.currentTimeMillis());
		if (context != null)
			session.setMaxInactiveInterval(getContext().getSessionTimeout() * 60);

		String id = standardSessionIdGenerator.generateSessionId();
//		if (id == null) {
//			id = standardSessionIdGenerator.generateSessionId();
//		}
		session.setId(id);
		sessions.put(id, session);
		sessionCounter++;
		return (session);

	}

	private HttpSession createEmptySession() {
		return new StandardSession(this);
	}

	public int getMaxInactiveInterval() {
		Context context = getContext();
		if (context == null) {
			return -1;
		}
		return context.getSessionTimeout() * 60;
	}

	private Context getContext() {
		return context;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getSessionCounter() {
		return sessionCounter;
	}

	public void setSessionCounter(int sessionCounter) {
		this.sessionCounter = sessionCounter;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public StandardSessionIdGenerator getStandardSessionIdGenerator() {
		return standardSessionIdGenerator;
	}

	public void setStandardSessionIdGenerator(StandardSessionIdGenerator standardSessionIdGenerator) {
		this.standardSessionIdGenerator = standardSessionIdGenerator;
	}

	public long getExpiredSessions() {
		return expiredSessions.get();
	}

	public void setExpiredSessions(long expiredSessions) {
		this.expiredSessions.set(expiredSessions);
	}
}
