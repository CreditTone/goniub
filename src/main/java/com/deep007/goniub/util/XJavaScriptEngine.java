package com.deep007.goniub.util;

import java.io.Reader;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class XJavaScriptEngine implements ScriptEngine {
	
	private ScriptEngine core;
	
	public XJavaScriptEngine() {
		ScriptEngineManager manager = new ScriptEngineManager();
		core = manager.getEngineByName("javascript");
	}

	@Override
	public Object eval(String script, ScriptContext context) throws ScriptException {
		return core.eval(script, context);
	}

	@Override
	public Object eval(Reader reader, ScriptContext context) throws ScriptException {
		return core.eval(reader, context);
	}

	@Override
	public Object eval(String script) throws ScriptException {
		while (true) {
			try {
				return core.eval(script);
			} catch (ScriptException e) {
				String varName = XCrawlerUtils.regexMatch(e.getMessage(), "\"(.+)\" is not defined", 1);
				if (varName != null) {
					put(varName, varName);
					continue;
				}else {
					break;
				}
			}
		}
		return null;
	}

	@Override
	public Object eval(Reader reader) throws ScriptException {
		return core.eval(reader);
	}

	@Override
	public Object eval(String script, Bindings n) throws ScriptException {
		return core.eval(script, n);
	}

	@Override
	public Object eval(Reader reader, Bindings n) throws ScriptException {
		return core.eval(reader, n);
	}

	@Override
	public void put(String key, Object value) {
		core.put(key, value);
	}

	@Override
	public Object get(String key) {
		return core.get(key);
	}

	@Override
	public Bindings getBindings(int scope) {
		return core.getBindings(scope);
	}

	@Override
	public void setBindings(Bindings bindings, int scope) {
		core.setBindings(bindings, scope);
	}

	@Override
	public Bindings createBindings() {
		return core.createBindings();
	}

	@Override
	public ScriptContext getContext() {
		return core.getContext();
	}

	@Override
	public void setContext(ScriptContext context) {
		core.setContext(context);
	}

	@Override
	public ScriptEngineFactory getFactory() {
		return core.getFactory();
	}
	
}
