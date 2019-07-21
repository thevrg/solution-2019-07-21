package hu.vrg.demo;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamingLogParser {
    public static IntStream getIdsByMessage(Reader xmlReader, String message) throws Exception {
        return createLogEntryStream(xmlReader)
                .filter(entry -> entry.getMessage().equals(message))
                .mapToInt(entry -> entry.getId());
    }

    public static Stream<LogEntry> createLogEntryStream(Reader reader) throws XMLStreamException {
        XMLEventReader eventReader = XMLInputFactory.newFactory().createXMLEventReader(reader);
        LogEntryIterator logEntryIterator = new LogEntryIterator(eventReader);
        Spliterator<LogEntry> spliterator = Spliterators.spliteratorUnknownSize(logEntryIterator, Spliterator.ORDERED | Spliterator.IMMUTABLE);
        return StreamSupport.stream(spliterator, false);
    }

    public static void main(String[] args) throws Exception {
        String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<log>\n" +
                        "    <entry id=\"1\">\n" +
                        "        <message>Application started</message>\n" +
                        "    </entry>\n" +
                        "    <entry id=\"2\">\n" +
                        "        <message>Application ended</message>\n" +
                        "    </entry>\n" +
                        "    <entry id=\"3\">\n" +
                        "        <message>Application ended</message>\n" +
                        "    </entry>\n" +
                        "</log>";

        getIdsByMessage(new StringReader(xml), "Application ended")
                .forEach(System.out::println);
    }
}
