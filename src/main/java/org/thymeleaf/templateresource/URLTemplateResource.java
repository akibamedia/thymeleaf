/*
 * =============================================================================
 * 
 *   Copyright (c) 2011-2014, The THYMELEAF team (http://www.thymeleaf.org)
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 * =============================================================================
 */
package org.thymeleaf.templateresource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import org.thymeleaf.util.StringUtils;
import org.thymeleaf.util.Validate;

/**
 *
 * @author Daniel Fern&aacute;ndez
 * @since 3.0.0
 * 
 */
public final class URLTemplateResource implements ITemplateResource, Serializable {


    private final URL url;
    private final String characterEncoding;



    public URLTemplateResource(final String path, final String characterEncoding) throws MalformedURLException {

        super();

        Validate.notEmpty(path, "Resource Path cannot be null or empty");
        // Character encoding CAN be null (system default will be used)

        this.url = new URL(path);
        this.characterEncoding = characterEncoding;

    }


    public URLTemplateResource(final URL url, final String characterEncoding) {

        super();

        Validate.notNull(url, "Resource URL cannot be null");
        // Character encoding CAN be null (system default will be used)

        this.url = url;
        this.characterEncoding = characterEncoding;

    }




    public String getName() {
        return this.url.toString();
    }




    public Reader reader() throws IOException {

        final InputStream inputStream = inputStream();

        if (!StringUtils.isEmptyOrWhitespace(this.characterEncoding)) {
            return new InputStreamReader(inputStream, this.characterEncoding);
        }

        return new InputStreamReader(inputStream);

    }




    private InputStream inputStream() throws IOException {

        final URLConnection connection = this.url.openConnection();
        if (connection.getClass().getSimpleName().startsWith("JNLP")) {
            connection.setUseCaches(true);
        }

        final InputStream inputStream;
        try {
            inputStream = connection.getInputStream();
        } catch (final IOException e) {
            if (connection instanceof HttpURLConnection) {
                // disconnect() will probably close the underlying socket, which is fine given we had an exception
                ((HttpURLConnection) connection).disconnect();
            }
            throw e;
        }

        return inputStream;

    }




    public ITemplateResource relative(final String relativePath) throws IOException {

        Validate.notEmpty(relativePath, "Relative Path cannot be null or empty");

        // We will create a new URL using the current one as context, and the relative path as spec
        final URL relativeURL =
                new URL(this.url, (relativePath.charAt(0) == '/' ? relativePath.substring(1) : relativePath));

        return new URLTemplateResource(relativeURL, this.characterEncoding);

    }




    public boolean exists() {

        try {

            final String protocol = this.url.getProtocol();

            if ("file".equals(protocol)) {
                // This is a file system resource, so we will treat it as a file

                File file = null;
                try {
                    file = new File(toURI(this.url).getSchemeSpecificPart());
                } catch (final URISyntaxException ignored) {
                    // The URL was not a valid URI (not even after conversion)
                    file = new File(this.url.getFile());
                }

                return file.exists();

            }

            // Not a 'file' URL, so we need to try other less local methods (HTTP/generic connection)

            final URLConnection connection = this.url.openConnection();

            if (connection.getClass().getSimpleName().startsWith("JNLP")) {
                connection.setUseCaches(true);
            }

            if (connection instanceof HttpURLConnection) {

                final HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("HEAD"); // We don't want the document, just know if it exists

                int responseCode = httpConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return true;
                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    return false;
                }

                if (httpConnection.getContentLength() >= 0) {
                    // No status, but at least some content length info!
                    return true;
                }

                // At this point, there not much hope, so better even get rid of the socket
                httpConnection.disconnect();
                return false;

            }

            // Not an HTTP URL Connection, so let's try direclty obtaining content length info
            if (connection.getContentLength() >= 0) {
                return true;
            }

            // Last attempt: open (and then immediately close) the input stream (will raise IOException if not possible)
            final InputStream is = inputStream();
            is.close();

            return true;

        } catch (final IOException ignored) {
            return false;
        }

    }




    private static URI toURI(final URL url) throws URISyntaxException {

        String location = url.toString();
        if (location.indexOf(' ') == -1) {
            // No need to replace anything
            return new URI(location);
        }

        return new URI(StringUtils.replace(location, " ", "%20"));
    }



}
