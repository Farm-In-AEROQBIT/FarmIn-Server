package com.farmin.farminserver.domain.ftp.catm1ftp;

import com.farmin.farminserver.domain.catm1.boarscatm1.service.BoarsCatm1Service;
import com.farmin.farminserver.domain.catm1.finishingcatm1.service.FinishingCatm1Service;
import com.farmin.farminserver.domain.catm1.gestationcatm1.service.GestationCatm1Service;
import com.farmin.farminserver.domain.catm1.growingcatm1.service.GrowingCatm1Service;
import com.farmin.farminserver.domain.catm1.maternitycatm1.service.MaternityCatm1Service;
import com.farmin.farminserver.domain.catm1.pigletcatm1.service.PigletCatm1Service;
import com.farmin.farminserver.domain.catm1.reservecatm1.service.ReserveCatm1Service;
import com.farmin.farminserver.entity.barns.boars.BoarsRepository;
import com.farmin.farminserver.entity.barns.finishing.FinishingRepository;
import com.farmin.farminserver.entity.barns.gestation.GestationRepository;
import com.farmin.farminserver.entity.barns.growing.GrowingRepository;
import com.farmin.farminserver.entity.barns.maternity.MaternityRepository;
import com.farmin.farminserver.entity.barns.piglet.PigletRepository;
import com.farmin.farminserver.entity.barns.reserve.ReserveRepository;
import com.farmin.farminserver.entity.snfarminfo.SNFarmInfoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class CsvProcessorConfig {

    @Bean
    public Set<String> processedFiles() {
        return Collections.synchronizedSet(new HashSet<>());
    }

    @Bean
    public Catm1FTPLogFileProcessor catm1FTPLogFileProcessor(
            CsvProcessor csvProcessor,
            ExecutorService dbExecutor,
            Set<String> processedFiles,
            Map<String, String> sensorTableMapping,
            Map<String, String> sensorIdMapping
    ) {
        return new Catm1FTPLogFileProcessor(
                csvProcessor,
                dbExecutor,
                processedFiles,
                sensorTableMapping,
                sensorIdMapping
        );
    }

    @Bean
    public ExecutorService dbExecutor() {
        return Executors.newFixedThreadPool(5);
    }

    @Bean
    public Map<String, String> sensorTableMapping() {
        return Map.of(
                "BoarsCatm1Sensor", "Boars",
                "FinishingCatm1Sensor", "Finishing",
                "GestationCatm1Sensor", "Gestation",
                "GrowingCatm1Sensor", "Growing",
                "MaternityCatm1Sensor", "Maternity",
                "PigletCatm1Sensor", "Piglet",
                "ReserveCatm1Sensor", "Reserve"
        );
    }

    @Bean
    public Map<String, String> sensorIdMapping() {
        return Map.of(
                "BoarsCatm1Sensor", "BoarsID",
                "FinishingCatm1Sensor", "FinishingID",
                "GestationCatm1Sensor", "GestationID",
                "GrowingCatm1Sensor", "GrowingID",
                "MaternityCatm1Sensor", "MaternityID",
                "PigletCatm1Sensor", "PigletID",
                "ReserveCatm1Sensor", "ReserveID"
        );
    }

}
