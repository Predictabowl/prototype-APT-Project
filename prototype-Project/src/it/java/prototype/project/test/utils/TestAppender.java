package prototype.project.test.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

@Plugin(name = "Test", category = "Core", elementType = "appender", printObject = true)
public final class TestAppender extends AbstractAppender{

	private final List<LogEvent> logs;

	protected TestAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions,
			Property[] properties) {
		super(name, filter, layout, ignoreExceptions, properties);
		
		logs = new ArrayList<>();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void append(LogEvent event) {
		logs.add(event);
		// TODO Auto-generated method stub
		
	}
	
	public List<LogEvent> getLogs(){
		return logs;
	}
	
	@PluginFactory
	public static TestAppender createAppender(
			@PluginAttribute("name") String name,
			@PluginElement("Filter") final Filter filter) {
		
		return new TestAppender(name, filter, PatternLayout.createDefaultLayout(), true, Property.EMPTY_ARRAY);
		
	}

}
