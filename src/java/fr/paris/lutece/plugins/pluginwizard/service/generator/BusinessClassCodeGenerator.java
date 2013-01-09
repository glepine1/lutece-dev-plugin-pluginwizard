/*
 * Copyright (c) 2002-2012, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.pluginwizard.service.generator;

import fr.paris.lutece.plugins.pluginwizard.business.model.BusinessClass;
import fr.paris.lutece.plugins.pluginwizard.business.model.BusinessClassHome;
import fr.paris.lutece.plugins.pluginwizard.business.model.PluginModel;
import fr.paris.lutece.plugins.pluginwizard.service.SourceCodeGenerator;
import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.Collection;
import java.util.HashMap;


/**
 *
 * The business classes representing the business layer of the plugin is generated
 *
 */
public class BusinessClassCodeGenerator implements Generator
{
    /**
     * Visits the path and verifies if Business class is relevant
     * @param strPath The path representing the file structure of the zip
     * @param plugin The plugin
     * @param pluginModel the representation of the created plugin
     * @return The map with the name of the file and its corresponding content
     */
    @Override
    public HashMap generate( Plugin plugin, PluginModel pluginModel )
    {
        HashMap map = new HashMap(  );
        Collection<BusinessClass> listAllBusinessClasses = BusinessClassHome.getBusinessClassesByPlugin( pluginModel.getIdPlugin(  ),
                plugin );

        String strBasePath = "plugin-{plugin_name}/src/java/fr/paris/lutece/plugins/{plugin_name}/business/";
                strBasePath = strBasePath.replace( "{plugin_name}", pluginModel.getPluginName(  ) );

        for ( BusinessClass businessClass : listAllBusinessClasses )
        {
            for ( int i = 1; i < 10; i++ )
            {
                // Odd numbers for plugin
                if ( ( i % 2 ) != 0 )
                {
                    String strClassName = getBusinessClassName( businessClass.getBusinessClass(  ), i );
                    String strPath;
                    

                    strPath = strBasePath + strClassName + ".java";
                    if ( i == 9 )
                    {
                        // The test source code is in another directory
                        strPath = strPath.replace( "src", "src/test" );
                    }

                    String strSourceCode = SourceCodeGenerator.getSourceCode( businessClass, i );
                    strSourceCode = strSourceCode.replace( "&lt;", "<" );
                    strSourceCode = strSourceCode.replace( "&gt;", ">" );
                    map.put( strPath, strSourceCode );
                }
            }
        }

        return map;
    }

    /**
     * Returns the name of the business class file
     * @param strBusinessClass The business class
     * @param nBusinessClassType The business class type
     * @return The name of the business class file
     */
    private String getBusinessClassName( String strBusinessClass, int nBusinessClassType )
    {
        String strReturn;

        switch ( nBusinessClassType )
        {
            case 1:
                strReturn = strBusinessClass;

                break;

            case 3:
                strReturn = strBusinessClass + "DAO";

                break;

            case 5:
                strReturn = "I" + strBusinessClass + "DAO";

                break;

            case 7:
                strReturn = strBusinessClass + "Home";

                break;

            case 9:
                strReturn = strBusinessClass + "BusinessTest";

                break;

            default:
                strReturn = strBusinessClass;

                break;
        }

        return strReturn;
    }
}
