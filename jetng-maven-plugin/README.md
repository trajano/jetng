JET-NG Maven Plugin
===================

A Maven plugin that will process the `.jet` files as part of the Maven build.

## How to use

There is an [integration test][1] that shows how to use this.  To register the
plugin add the following to the `pom.xml`.

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

The `.jet` files are expected to be in `src/main/jetng`.

[1]: https://github.com/trajano/jetng/tree/master/jetng-maven-plugin/src/it/sample
