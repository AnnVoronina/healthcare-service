package ru.netology.patient;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MedicalServiceTest {

    static PatientInfoRepository patientInfoRepository;
    static SendAlertService sendAlertService;
    static MedicalService medicalService;
    static String message = "Warning, patient with id: id1, need help";

    @BeforeAll

    public static void initSuit() {
        patientInfoRepository = mock(PatientInfoFileRepository.class);
        sendAlertService = mock(SendAlertService.class);
        PatientInfo patientInfo = new PatientInfo("id1", "Ivan", "Ivanov",
                LocalDate.of(1999, 07, 23),
                new HealthInfo(new BigDecimal(35.5), new BloodPressure(120, 150)));

        when(patientInfoRepository.getById("id1")).thenReturn((patientInfo));
    }
    @Test
    public void checkTemperatureHigh(){
        medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkTemperature("id1",new BigDecimal(39.0));
        Mockito.verify(sendAlertService,Mockito.times(1)).send(message);
    }

//    @Test
//    public void checkBloodPressureTest() {
//        medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
//        assertEquals("Warning, patient with id: 1, need help",
//                medicalService.checkBloodPressure("id1",new BloodPressure(120,150)));
//
//    }


    @Test
    public void checkBloodPressureLow(){
        medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkBloodPressure("id1", new BloodPressure(120,80));
        Mockito.verify(sendAlertService,Mockito.times(0)).send(message);
    }
    @Test
    public void checkBloodPressureHigh(){
        medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkBloodPressure("id1", new BloodPressure(150,120));
        Mockito.verify(sendAlertService,Mockito.times(1)).send(message);
    }
    @Test
    public void checkTemperatureLow(){
        medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkTemperature("id1",new BigDecimal(36.0));
        Mockito.verify(sendAlertService,Mockito.times(0)).send(message);
    }


}
