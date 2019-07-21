package hu.vrg.demo;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

public class LogEntryIterator implements Iterator<LogEntry> {

    private Optional<LogEntry> next;

    private XMLEventReader eventReader;

    private boolean fetched = false;

    public LogEntryIterator(XMLEventReader eventReader) {
        this.eventReader = eventReader;
    }

    private Optional<LogEntry> fetchNext() {
        try {
            LogEntry logEntry = null;
            Integer id = null;
            boolean inMessage = false;
            StringBuilder message = new StringBuilder();
            do {
                XMLEvent xmlEvent = eventReader.nextEvent();
                if (xmlEvent.isStartElement()) {
                    StartElement startElement = (StartElement) xmlEvent;
                    if (startElement.getName().getLocalPart().equals("entry")) {
                        id = Integer.valueOf(startElement.getAttributeByName(new QName("id")).getValue());
                    } else if (startElement.getName().getLocalPart().equals("message")) {
                        inMessage = true;
                    }
                } else if (xmlEvent.isEndElement()) {
                    EndElement endElement = (EndElement) xmlEvent;
                    if (endElement.getName().getLocalPart().equals("message")) {
                        inMessage = false;
                        logEntry = new LogEntry(id, message.toString());
                    }
                } else if (inMessage && xmlEvent.isCharacters()) {
                    Characters characters = (Characters) xmlEvent;
                    message.append(characters.getData());
                } else if (xmlEvent.isEndDocument()) {
                    fetched = true;
                    return Optional.empty();
                }
            } while (logEntry == null);
            fetched = true;
            return Optional.of(logEntry);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasNext() {
        if (!fetched) {
            next = fetchNext();
        }
        return next.isPresent();
    }

    @Override
    public LogEntry next() {
        if (!fetched) {
            next = fetchNext();
        }
        LogEntry logEntry = next.orElseThrow(() -> new NoSuchElementException("No such element"));
        fetched = false;
        return logEntry;
    }
}
