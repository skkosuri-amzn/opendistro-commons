package com.amazon.opendistroforelasticsearch.security.rolesinfo;

import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.ToXContentObject;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;


/**
 * Used by TransportRolesInfoAction in opendistro_security plugin.
 */
public class RolesInfoResponse extends ActionResponse implements ToXContentObject {

    private String userName = "";
    private ArrayList<String> roles = new ArrayList<>();
    private static final ParseField USER_NAME = new ParseField("user_name");
    private static final ParseField ROLES = new ParseField("roles");

    public RolesInfoResponse() {
        super();
    }

    public RolesInfoResponse(String user, Set<String> setRoles) {
        super();
        userName = user;
        roles.addAll(setRoles);
    }

    public RolesInfoResponse(StreamInput in) throws IOException {
        super(in);
        userName = in.readString();
        Arrays.stream(in.readOptionalStringArray()).forEach(a -> roles.add(a));
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(userName);
        String [] arr = new String[roles.size()];
        out.writeOptionalStringArray(roles.toArray(arr));
    }

    @Override
    public final XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(USER_NAME.getPreferredName(), userName);
        builder.field(ROLES.getPreferredName(), roles);
        builder.endObject();
        return builder;
    }

    /**
     * Roles are null when 1/ No opendistro roles are configured. 2/ security plugin is not installed.
     * return role1,role2,role3
     */
    @Override
    public String toString() {
        return userName + "|" + String.join(",", roles);
    }

    public String getUserName() {
        return userName;
    }

    public String getRolesString() {
        return (roles == null) ? "" : String.join(",", roles);
    }
}
