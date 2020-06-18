package com.amazon.opendistroforelasticsearch.security.rolesinfo;

import org.elasticsearch.common.Strings;
import org.elasticsearch.common.io.stream.BytesStreamOutput;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class RolesInfoResponseTest {

    @Test
    public void testRolesInfoResponseEmpty() throws IOException {
        RolesInfoResponse response = new RolesInfoResponse();
        String json = Strings.toString(response.toXContent(JsonXContent.contentBuilder(), ToXContent.EMPTY_PARAMS));
        Assert.assertTrue(json.contains("\"user_name\":\"\""));
        Assert.assertTrue(json.contains("\"roles\":[]"));
    }

    @Test
    public void testRolesInfoResponseWithParams() throws IOException {
        Set<String> roles = new HashSet<>();
        roles.add("all_access");
        roles.add("own_role");

        RolesInfoResponse response = new RolesInfoResponse("kirk", roles);
        String json = Strings.toString(response.toXContent(JsonXContent.contentBuilder(), ToXContent.EMPTY_PARAMS));

        Assert.assertTrue(json.contains("\"user_name\":\"kirk\""));
        Assert.assertTrue(json.contains("\"roles\":[\"own_role\",\"all_access\"]"));
    }

    @Test
    public void testRolesInfoResponseWithEmptyStream() throws IOException {

        RolesInfoResponse rOut = new RolesInfoResponse();
        BytesStreamOutput out = new BytesStreamOutput();
        rOut.writeTo(out);

        StreamInput in = StreamInput.wrap(out.bytes().toBytesRef().bytes);
        RolesInfoResponse rIn = new RolesInfoResponse(in);
        String json = Strings.toString(rIn.toXContent(JsonXContent.contentBuilder(), ToXContent.EMPTY_PARAMS));

        Assert.assertTrue(json.contains("\"user_name\":\"\""));
        Assert.assertTrue(json.contains("\"roles\":[]"));
    }
    @Test
    public void testRolesInfoResponseWithStream() throws IOException {

        Set<String> roles = new HashSet<>();
        roles.add("all_access");
        roles.add("own_role");

        RolesInfoResponse rOut = new RolesInfoResponse("kirk", roles);
        BytesStreamOutput out = new BytesStreamOutput();
        rOut.writeTo(out);

        StreamInput in = StreamInput.wrap(out.bytes().toBytesRef().bytes);
        RolesInfoResponse rIn = new RolesInfoResponse(in);

        String json = Strings.toString(rIn.toXContent(JsonXContent.contentBuilder(), ToXContent.EMPTY_PARAMS));
        Assert.assertTrue(json.contains("\"user_name\":\"kirk\""));
        Assert.assertTrue(json.contains("\"roles\":[\"own_role\",\"all_access\"]"));
    }
}
