package me.j360.rpc.core;

import java.util.Enumeration;

/**
 * Package: me.j360.rpc.core
 * User: min_xu
 * Date: 2017/6/1 下午2:07
 * 说明：
 */
public interface FilterConfig {


    /**
     * Returns the filter-name of this filter as defined in the deployment
     * descriptor.
     */
    public String getFilterName();


    /**
     * Returns a <code>String</code> containing the value of the
     * named initialization parameter, or <code>null</code> if
     * the initialization parameter does not exist.
     *
     * @param name a <code>String</code> specifying the name of the
     * initialization parameter
     *
     * @return a <code>String</code> containing the value of the
     * initialization parameter, or <code>null</code> if
     * the initialization parameter does not exist
     */
    public String getInitParameter(String name);


    /**
     * Returns the names of the filter's initialization parameters
     * as an <code>Enumeration</code> of <code>String</code> objects,
     * or an empty <code>Enumeration</code> if the filter has
     * no initialization parameters.
     *
     * @return an <code>Enumeration</code> of <code>String</code> objects
     * containing the names of the filter's initialization parameters
     */
    public Enumeration<String> getInitParameterNames();

}
