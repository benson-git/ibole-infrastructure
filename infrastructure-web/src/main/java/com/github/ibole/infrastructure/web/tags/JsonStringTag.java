package com.github.ibole.infrastructure.web.tags;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import java.io.IOException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * This tag writes a Javascript variable declaration named {@code varName} with
 * <i>Json-serialized</i> {@code object} as value.
 */
public final class JsonStringTag extends SimpleTagSupport {
  
  private String varName;

  private Object object;

  /**
   * Creates a new {@code JsonStringTag}.
   */
  public JsonStringTag() {
    // Empty constructor
  }

  /**
   * Writes the tag.
   * 
   * @throws JspException if an error occurs serializing the object
   * @throws IOException if an I/O error occurs
   */
  @SuppressWarnings("nls")
  @Override
  public void doTag() throws JspException, IOException {
    final JspContext context = getJspContext();
    final JspWriter out = context.getOut();
    final StringBuilder sb = new StringBuilder();
    sb.append("<script type=\"text/javascript\">");
    sb.append("var ");
    sb.append(varName);
    String serialized = null;
    try {
      serialized = JSON.toJSONString(object);
    } catch (final JSONException ex) {
      throw new JspException("error serializing object", ex);
    }
    sb.append(" = '{\"items\":");
    sb.append(serialized.replace("\'", "\\'"));
    sb.append("}';</script>");
    out.write(sb.toString());
  }

  /**
   * Assigns the given variable name to this {@code tag}.
   * 
   * @param varName the variable name to assign
   */
  public void setVarName(final String varName) {
    this.varName = varName;
  }

  /**
   * Assigns the given object to serialize to this {@code tag}.
   * 
   * @param object the object to serialize
   */
  public void setObject(final Object object) {
    this.object = object;
  }
}
