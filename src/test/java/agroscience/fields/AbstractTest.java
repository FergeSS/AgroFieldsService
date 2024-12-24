package agroscience.fields;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = PostgreTestContainerConfig.Initializer.class)
@ActiveProfiles("test")
public abstract class AbstractTest {

}