package com.hospital;

import com.hospital.service.PatientService;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PatientServiceTest {

    @Test
    public void testGetAllPatients() {
        PatientService service = new PatientService();
        Assert.assertTrue(service.getAllPatients().size() > 0);
    }
}