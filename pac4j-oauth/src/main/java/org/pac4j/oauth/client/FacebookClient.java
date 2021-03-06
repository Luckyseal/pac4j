package org.pac4j.oauth.client;

import com.github.scribejava.apis.FacebookApi;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.oauth.exception.OAuthCredentialsException;
import org.pac4j.oauth.profile.facebook.FacebookProfileCreator;
import org.pac4j.oauth.profile.facebook.FacebookProfileDefinition;
import org.pac4j.oauth.profile.facebook.FacebookProfile;

/**
 * <p>This class is the OAuth client to authenticate users in Facebook.</p>
 * <p>By default, the following <i>scope</i> is requested to Facebook: user_likes, user_about_me, user_birthday, user_education_history,
 * email, user_hometown, user_relationship_details, user_location, user_religion_politics, user_relationships, user_website and
 * user_work_history.</p>
 * <p>The <i>scope</i> can be defined to require permissions from the user and retrieve fields from Facebook, by using the
 * {@link #setScope(String)} method.</p>
 * <p>By default, the following <i>fields</i> are requested to Facebook: id, name, first_name, middle_name, last_name, gender, locale,
 * languages, link, third_party_id, timezone, updated_time, verified, about, birthday, education, email, hometown, interested_in,
 * location, political, favorite_athletes, favorite_teams, quotes, relationship_status, religion, significant_other, website and work.</p>
 * <p>The <i>fields</i> can be defined and requested to Facebook, by using the {@link #setFields(String)} method.</p>
 * <p>The number of results can be limited by using the {@link #setLimit(int)} method.</p>
 * <p>An extended access token can be requested by setting <code>true</code> on the {@link #setRequiresExtendedToken(boolean)} method.</p>
 * <p>It returns a {@link org.pac4j.oauth.profile.facebook.FacebookProfile}.</p>
 * <p>More information at http://developers.facebook.com/docs/reference/api/user/</p>
 * <p>More information at https://developers.facebook.com/docs/howtos/login/extending-tokens/</p>
 *
 * @author Jerome Leleu
 * @author Mehdi BEN HAJ ABBES
 * @since 1.0.0
 */
public class FacebookClient extends OAuth20Client<FacebookProfile> {

    public final static String DEFAULT_FIELDS = "id,name,first_name,middle_name,last_name,gender,locale,languages,link,third_party_id," 
        + "timezone,updated_time,verified,about,birthday,education,email,hometown,interested_in,location,political,favorite_athletes,"
        + "favorite_teams,quotes,relationship_status,religion,significant_other,website,work";

    protected String fields = DEFAULT_FIELDS;

    public final static String DEFAULT_SCOPE = "user_likes,user_about_me,user_birthday,user_education_history,email,user_hometown,"
        + "user_relationship_details,user_location,user_religion_politics,user_relationships,user_website,user_work_history";

    protected String scope = DEFAULT_SCOPE;

    protected int limit = FacebookProfileDefinition.DEFAULT_LIMIT;

    protected boolean requiresExtendedToken = false;

    protected boolean useAppsecretProof = false;

    public FacebookClient() {
    }

    public FacebookClient(final String key, final String secret) {
        setKey(key);
        setSecret(secret);
    }

    @Override
    protected void clientInit(final WebContext context) {
        CommonHelper.assertNotBlank("fields", this.fields);
        configuration.setApi(FacebookApi.instance());
        configuration.setProfileDefinition(new FacebookProfileDefinition());
        configuration.setScope(scope);
        configuration.setHasBeenCancelledFactory(ctx -> {
            final String error = ctx.getRequestParameter(OAuthCredentialsException.ERROR);
            final String errorReason = ctx.getRequestParameter(OAuthCredentialsException.ERROR_REASON);
            // user has denied permissions
            if ("access_denied".equals(error) && "user_denied".equals(errorReason)) {
                return true;
            } else {
                return false;
            }
        });
        configuration.setWithState(true);
        setConfiguration(configuration);
        defaultProfileCreator(new FacebookProfileCreator(configuration));

        super.clientInit(context);
    }

    public void setStateData(final String stateData) {
        configuration.setStateData(stateData);
    }

    public String getStateData() {
        return configuration.getStateData();
    }

    public void setUseAppSecretProof(final boolean useSecret) {
        this.useAppsecretProof = useSecret;
    }

    public boolean getUseAppSecretProof() {
        return this.useAppsecretProof;
    }

    public String getScope() {
        return this.scope;
    }

    public void setScope(final String scope) {
        this.scope = scope;
    }

    public String getFields() {
        return this.fields;
    }

    public void setFields(final String fields) {
        this.fields = fields;
    }

    public int getLimit() {
        return this.limit;
    }

    public void setLimit(final int limit) {
        this.limit = limit;
    }

    public boolean isRequiresExtendedToken() {
        return this.requiresExtendedToken;
    }

    public void setRequiresExtendedToken(final boolean requiresExtendedToken) {
        this.requiresExtendedToken = requiresExtendedToken;
    }
}
