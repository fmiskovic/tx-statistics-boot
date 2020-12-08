package org.tx.statistics.service;

import org.tx.statistics.date.DateProvider;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public abstract class AbstractServiceTest {

    @Autowired
    protected DateProvider dateProvider;
}
