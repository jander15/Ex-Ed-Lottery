package net.aspenk12.exed.alg.members;

import net.aspenk12.exed.alg.containers.Application;
import net.aspenk12.exed.alg.containers.MockApplication;
import net.aspenk12.exed.alg.containers.MockProfile;

public class MockStudent extends Student{
    public MockStudent(Profile profile, Application application) {
        super(profile, application);
    }

    public MockStudent(MockApplication mockApplication){
        super(new MockProfile(), mockApplication);
    }

    public MockProfile getProfile(){
        return (MockProfile) profile;
    }
}
