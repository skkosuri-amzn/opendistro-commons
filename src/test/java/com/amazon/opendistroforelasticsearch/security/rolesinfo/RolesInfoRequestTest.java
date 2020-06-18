package com.amazon.opendistroforelasticsearch.security.rolesinfo;

import org.elasticsearch.common.Strings;
import org.elasticsearch.common.io.stream.BytesStreamOutput;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class RolesInfoRequestTest {

    @Test
    public void testRolesInfoRequest() throws IOException {

        RolesInfoRequest rOut = new RolesInfoRequest();
        BytesStreamOutput out = new BytesStreamOutput();
        rOut.writeTo(out);

        StreamInput in = StreamInput.wrap(out.bytes().toBytesRef().bytes);
        RolesInfoRequest rIn = new RolesInfoRequest(in);
        String json = Strings.toString(rIn.toXContent(JsonXContent.contentBuilder(), ToXContent.EMPTY_PARAMS));

        Assert.assertNotNull(new RolesInfoRequest());
        Assert.assertTrue(json.contains(""));
    }
}
