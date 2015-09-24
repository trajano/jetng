# JET-NG Annotation Processor 

An annotation processor that creates annotation processor code that utilizes 
JET-NG.

## Usage Note

The following plugins need to be configured in the `pom.xml` for this to work.

    <plugin>
      <groupId>net.trajano.mojo</groupId>
      <artifactId>jetng-maven-plugin</artifactId>
      <executions>
        <execution>
          <goals>
            <goal>compile</goal>
          </goals>
        </execution>
      </executions>
    </plugin>

    <plugin>
      <artifactId>maven-resources-plugin</artifactId>
      <executions>
        <execution>
          <phase>process-classes</phase>
          <goals>
            <goal>copy-resources</goal>
          </goals>
          <configuration>
            <outputDirectory>${project.build.outputDirectory}</outputDirectory>
            <resources>
              <resource>
                <directory>${project.build.directory}/generated-sources/annotations</directory>
                <includes>
                  <include>META-INF/services/javax.annotation.processing.Processor</include>
                </includes>
              </resource>
            </resources>
          </configuration>
        </execution>
      </executions>
    </plugin>

The `maven-resources-plugin` is used to copy the generated
`javax.annotation.processing.Processor` file into the `target/classes` 
folder as it is not automatically copied after it is generated from the `compile
phase.
  
