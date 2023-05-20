package com.javarush.task.task39.task3913;

import com.javarush.task.task39.task3913.query.*;

import javax.xml.stream.events.EndElement;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser implements IPQuery, UserQuery, DateQuery, EventQuery, QLQuery {
    private Path logDir;

    private List<LogEntity> logEntities = new ArrayList<>();
    private DateFormat simpleDateFormat = new SimpleDateFormat("d.M.yyyy H:m:s");

    public LogParser(Path logDir) {
        this.logDir = logDir;
        readLogs();
    }

    @Override
    public int getNumberOfUniqueIPs(Date after, Date before) {

        return getUniqueIPs(after, before).size();
    }

    @Override
    public Set<String> getUniqueIPs(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before)) {
                result.add(logEntity.getIp());
            }
        }
        return result;
    }

    @Override
    public Set<String> getIPsForUser(String user, Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before) && user.equals(logEntity.getUser())) {
                result.add(logEntity.getIp());
            }
        }
        return result;
    }

    @Override
    public Set<String> getIPsForEvent(Event event, Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before) && event.equals(logEntity.getEvent())) {
                result.add(logEntity.getIp());
            }
        }
        return result;
    }

    @Override
    public Set<String> getIPsForStatus(Status status, Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before) && status.equals(logEntity.getStatus())) {
                result.add(logEntity.getIp());
            }
        }
        return result;
    }

    private boolean dateBetweenDates(Date current, Date after, Date before) {
        if (after == null) {
            after = new Date(0);
        }
        if (before == null) {
            before = new Date(Long.MAX_VALUE);
        }
        return current.after(after) && current.before(before);

    }

    private void readLogs() {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(logDir)) {
            for (Path file : directoryStream) {
                if (file.toString().toLowerCase().endsWith(".log")) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                        while (reader.ready()) {
                            String line = reader.readLine();
                            String[] split = line.split("\t");
                            if (split.length != 5) {
                                continue;
                            }
                            String ip = split[0];
                            String user = split[1];
                            Date date = readDate(split[2]);
                            Event event = readEvent(split[3]);
                            int eventAdditionalParameter = -1;
                            if (event.equals(Event.SOLVE_TASK) || event.equals(Event.DONE_TASK)) {
                                eventAdditionalParameter = readAdditionalParameter(split[3]);
                            }
                            Status status = readStatus(split[4]);
                            LogEntity logEntity = new LogEntity(ip, user, date, event, eventAdditionalParameter, status);
                            logEntities.add(logEntity);
                        }
                    }
                }
            }
        } catch (IOException e) {

        }
    }

    private int readAdditionalParameter(String line) {
        String[] split = line.split(" ");
        String number = split[1].trim();
        return Integer.parseInt(number);
    }

    private Status readStatus(String line) {
        Status status = null;
        switch (line) {
            case "OK" :
                status = Status.OK;
                break;
            case "FAILED" :
                status = Status.FAILED;
                break;
            case "ERROR" :
                status = Status.ERROR;
                break;
        }
        return status;
    }

    private Date readDate(String line) {
        Date date = null;
        try {
            date = simpleDateFormat.parse(line);
        } catch (ParseException e) {

        }
        return date;
    }

    private Event readEvent(String line) {
        Event event = null;
        if (line.contains("SOLVE_TASK")) {
            event = Event.SOLVE_TASK;
        } else if (line.contains("DONE_TASK")) {
            event = Event.DONE_TASK;
        } else {
            switch (line) {
                case "LOGIN" :
                    event = Event.LOGIN;
                    break;
                case "DOWNLOAD_PLUGIN" :
                    event = Event.DOWNLOAD_PLUGIN;
                    break;
                case "WRITE_MESSAGE" :
                    event = Event.WRITE_MESSAGE;
                    break;
            }
        }
        return event;
    }

    @Override
    public Set<String> getAllUsers() {
        Set<String> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            result.add(logEntity.getUser());
        }
        return result;
    }

    @Override
    public int getNumberOfUsers(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before)) {
                result.add(logEntity.getUser());
            }
        }
        return result.size();
    }

    @Override
    public int getNumberOfUserEvents(String user, Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before) && user.equals(logEntity.getUser())) {
                result.add(logEntity.getEvent().name());
            }
        }
        return result.size();
    }

    @Override
    public Set<String> getUsersForIP(String ip, Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before) && ip.equals(logEntity.getIp())) {
                result.add(logEntity.getUser());
            }
        }
        return result;
    }

    @Override
    public Set<String> getLoggedUsers(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before) && logEntity.getEvent().equals(Event.LOGIN)) {
                result.add(logEntity.getUser());
            }
        }
        return result;
    }

    @Override
    public Set<String> getDownloadedPluginUsers(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before) && logEntity.getEvent().equals(Event.DOWNLOAD_PLUGIN)) {
                result.add(logEntity.getUser());
            }
        }
        return result;
    }

    @Override
    public Set<String> getWroteMessageUsers(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before) && logEntity.getEvent().equals(Event.WRITE_MESSAGE)) {
                result.add(logEntity.getUser());
            }
        }
        return result;
    }

    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before) && logEntity.getEvent().equals(Event.SOLVE_TASK)) {
                    result.add(logEntity.getUser());
            }
        }
        return result;
    }

    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before, int task) {
        Set<String> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before) && logEntity.getEventAdditionalParameter() == task &&
                    logEntity.getEvent().equals(Event.SOLVE_TASK)) {
                result.add(logEntity.getUser());
            }
        }
        return result;
    }

    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before) && logEntity.getEvent().equals(Event.DONE_TASK)) {
                result.add(logEntity.getUser());
            }
        }
        return result;
    }

    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before, int task) {
        Set<String> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before) && logEntity.getEventAdditionalParameter() == task &&
                    logEntity.getEvent().equals(Event.DONE_TASK)) {
                result.add(logEntity.getUser());
            }
        }
        return result;
    }

    @Override
    public Set<Date> getDatesForUserAndEvent(String user, Event event, Date after, Date before) {
        Set<Date> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (logEntity.getUser().equals(user) && logEntity.getEvent().equals(event)) {
                if (dateBetweenDates(logEntity.getDate(), after, before)) {
                    result.add(logEntity.getDate());
                }
            }
        }
        return result;
    }

    @Override
    public Set<Date> getDatesWhenSomethingFailed(Date after, Date before) {
        Set<Date> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before) && logEntity.getStatus().equals(Status.FAILED)) {
                    result.add(logEntity.getDate());
            }
        }
        return result;
    }

    @Override
    public Set<Date> getDatesWhenErrorHappened(Date after, Date before) {
        Set<Date> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before) && logEntity.getStatus().equals(Status.ERROR)) {
                result.add(logEntity.getDate());
            }
        }
        return result;
    }

    @Override
    public Date getDateWhenUserLoggedFirstTime(String user, Date after, Date before) {
        Set<Date> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before) && logEntity.getUser().equals(user) &&
                    logEntity.getEvent().equals(Event.LOGIN)) {
                result.add(logEntity.getDate());
            }
        }
        if (result.size() == 0) {
            return null;
        }
        Date minDate = result.iterator().next();
        for (Date date : result) {
            if (date.getTime() < minDate.getTime()) {
                minDate = date;
            }
        }
        return minDate;
    }

    @Override
    public Date getDateWhenUserSolvedTask(String user, int task, Date after, Date before) {
        Set<Date> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before) && logEntity.getUser().equals(user) &&
            logEntity.getEventAdditionalParameter() == task && logEntity.getEvent().equals(Event.SOLVE_TASK)) {
                result.add(logEntity.getDate());
            }
        }
        if (result.size() == 0) {
            return null;
        }
        Date minDate = result.iterator().next();
        for (Date date : result) {
            if (date.getTime() < minDate.getTime()) {
                minDate = date;
            }
        }
        return minDate;
    }

    @Override
    public Date getDateWhenUserDoneTask(String user, int task, Date after, Date before) {
        Set<Date> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before) && logEntity.getUser().equals(user) &&
                    logEntity.getEventAdditionalParameter() == task && logEntity.getEvent().equals(Event.DONE_TASK)) {
                result.add(logEntity.getDate());
            }
        }
        if (result.size() == 0) {
            return null;
        }
        Date minDate = result.iterator().next();
        for (Date date : result) {
            if (date.getTime() < minDate.getTime()) {
                minDate = date;
            }
        }
        return minDate;
    }

    @Override
    public Set<Date> getDatesWhenUserWroteMessage(String user, Date after, Date before) {
        Set<Date> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (logEntity.getUser().equals(user) && logEntity.getEvent().equals(Event.WRITE_MESSAGE)) {
                if (dateBetweenDates(logEntity.getDate(), after, before)) {
                    result.add(logEntity.getDate());
                }
            }
        }
        return result;
    }

    @Override
    public Set<Date> getDatesWhenUserDownloadedPlugin(String user, Date after, Date before) {
        Set<Date> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (logEntity.getUser().equals(user) && logEntity.getEvent().equals(Event.DOWNLOAD_PLUGIN)) {
                if (dateBetweenDates(logEntity.getDate(), after, before)) {
                    result.add(logEntity.getDate());
                }
            }
        }
        return result;
    }

    @Override
    public int getNumberOfAllEvents(Date after, Date before) {
        return getAllEvents(after, before).size();
    }

    @Override
    public Set<Event> getAllEvents(Date after, Date before) {
        Set<Event> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before)) {
                result.add(logEntity.getEvent());
            }
        }
        return result;
    }

    @Override
    public Set<Event> getEventsForIP(String ip, Date after, Date before) {
        Set<Event> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before) && logEntity.getIp().equals(ip)) {
                result.add(logEntity.getEvent());
            }
        }
        return result;
    }

    @Override
    public Set<Event> getEventsForUser(String user, Date after, Date before) {
        Set<Event> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before) && logEntity.getUser().equals(user)) {
                result.add(logEntity.getEvent());
            }
        }
        return result;
    }

    @Override
    public Set<Event> getFailedEvents(Date after, Date before) {
        Set<Event> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before) && logEntity.getStatus().equals(Status.FAILED)) {
                result.add(logEntity.getEvent());
            }
        }
        return result;
    }

    @Override
    public Set<Event> getErrorEvents(Date after, Date before) {
        Set<Event> result = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before) && logEntity.getStatus().equals(Status.ERROR)) {
                result.add(logEntity.getEvent());
            }
        }
        return result;
    }

    @Override
    public int getNumberOfAttemptToSolveTask(int task, Date after, Date before) {
        ArrayList<Event> result = new ArrayList<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before) && logEntity.getEvent().equals(Event.SOLVE_TASK) &&
            logEntity.getEventAdditionalParameter() == task) {
                result.add(logEntity.getEvent());
            }
        }
        return result.size();
    }

    @Override
    public int getNumberOfSuccessfulAttemptToSolveTask(int task, Date after, Date before) {
        ArrayList<Event> result = new ArrayList<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before) && logEntity.getEvent().equals(Event.DONE_TASK) &&
                    logEntity.getEventAdditionalParameter() == task) {
                result.add(logEntity.getEvent());
            }
        }
        return result.size();
    }

    @Override
    public Map<Integer, Integer> getAllSolvedTasksAndTheirNumber(Date after, Date before) {
        Map<Integer, Integer> result = new HashMap<>();
        Set<Integer> tasks = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before) && logEntity.getEvent().equals(Event.SOLVE_TASK)) {
                tasks.add(logEntity.getEventAdditionalParameter());
            }
        }
        for (Integer number : tasks) {
            int value = getNumberOfAttemptToSolveTask(number, after, before);
            result.put(number, value);
        }
        return result;
    }

    @Override
    public Map<Integer, Integer> getAllDoneTasksAndTheirNumber(Date after, Date before) {
        Map<Integer, Integer> result = new HashMap<>();
        Set<Integer> tasks = new HashSet<>();
        for (LogEntity logEntity : logEntities) {
            if (dateBetweenDates(logEntity.getDate(), after, before) && logEntity.getEvent().equals(Event.DONE_TASK)) {
                tasks.add(logEntity.getEventAdditionalParameter());
            }
        }
        for (Integer number : tasks) {
            int value = getNumberOfSuccessfulAttemptToSolveTask(number, after, before);
            result.put(number, value);
        }
        return result;
    }

    @Override
    public Set<Object> execute(String query) {
        Set<Object> result = new HashSet<>();
        String[] splitFirst = query.split(" ");
        if (splitFirst.length > 12) {
            String[] splitSecond = query.split("=");
            String[] split = splitSecond[0].trim().split(" ");
            String field1 = split[1];
            String field2 = split[3];
            String[] splitThird = splitSecond[1].trim().split("and");
            String value1 = splitThird[0].trim().replaceAll("\"", "").trim();
            String beforeString = splitThird[2].trim().replaceAll("\"", "").trim();
            String[] splitFourth = splitThird[1].split("between");
            String afterString = splitFourth[1].trim().replaceAll("\"", "").trim();
            Date after = null;
            Date before = null;
            try {
                before = simpleDateFormat.parse(beforeString);
                after = simpleDateFormat.parse(afterString);
            } catch (ParseException e) {

            }
            for (LogEntity logEntity : logEntities) {
                if (field2.equals("date")) {
                    try {
                        if (logEntity.getDate().getTime() == simpleDateFormat.parse(value1).getTime() &&
                                dateBetweenDates(logEntity.getDate(), after, before)) {
                            result.add(getCurrentValue(logEntity, field1));
                        }
                    } catch (ParseException e) {

                    }
                } else {
                    if (getCurrentValue(logEntity, field2).toString().equals(value1) &&
                            dateBetweenDates(logEntity.getDate(), after, before)) {
                        result.add(getCurrentValue(logEntity, field1));
                    }
                }
            }
            return result;
        }else if (splitFirst.length > 2) {
            String[] splitSecond = query.split("=");
            String[] split = splitSecond[0].trim().split(" ");
            String field1 = split[1];
            String field2 = split[3];
            String value1 = splitSecond[1].trim().replaceAll("\"", "").trim();
            for (LogEntity logEntity : logEntities) {
                if (field2.equals("date")) {
                    try {
                        if (logEntity.getDate().getTime() == simpleDateFormat.parse(value1).getTime()) {
                        result.add(getCurrentValue(logEntity, field1));
                    }
                    } catch (ParseException e) {

                    }
                } else {
                    if (getCurrentValue(logEntity, field2).toString().equals(value1)) {
                        result.add(getCurrentValue(logEntity, field1));
                    }
                }
            }
            return result;
        }
        String field = null;
        Pattern pattern = Pattern.compile("get (ip|user|date|event|status)");
        Matcher matcher = pattern.matcher(query);
        matcher.find();
        field = matcher.group(1);
        for (LogEntity logEntity : logEntities) {
            result.add(getCurrentValue(logEntity, field));
        }

        return result;
    }


    private Object getCurrentValue(LogEntity logEntity, String field) {
        Object value = null;
        switch (field) {
            case "ip" : {
                Command method = new GetIpCommand(logEntity);
                value = method.execute();
                break;
            }
            case "user" : {
                Command method = new GetUserCommand(logEntity);
                value = method.execute();
                break;
            }
            case "date" : {
                Command method = new GetDateCommand(logEntity);
                value = method.execute();
                break;
            }
            case "event" : {
                Command method = new GetEventCommand(logEntity);
                value = method.execute();
                break;
            }
            case "status" : {
                Command method = new GetStatusCommand(logEntity);
                value = method.execute();
                break;
            }
        }
        return value;
    }

    private class LogEntity {
        private String ip;
        private String user;
        private Date date;
        private Event event;
        private int eventAdditionalParameter;
        private Status status;

        public LogEntity(String ip, String user, Date date, Event event, int eventAdditionalParameter, Status status) {
            this.ip = ip;
            this.user = user;
            this.date = date;
            this.event = event;
            this.eventAdditionalParameter = eventAdditionalParameter;
            this.status = status;
        }

        public String getIp() {
            return ip;
        }

        public String getUser() {
            return user;
        }

        public Date getDate() {
            return date;
        }

        public Event getEvent() {
            return event;
        }

        public int getEventAdditionalParameter() {
            return eventAdditionalParameter;
        }

        public Status getStatus() {
            return status;
        }
    }

    private abstract class Command {
        protected LogEntity logEntity;
        abstract Object execute();
    }

    private class GetIpCommand extends Command {
        public GetIpCommand(LogEntity logEntity) {
            this.logEntity = logEntity;
        }

        @Override
        Object execute() {
            return logEntity.getIp();
        }
    }

    private class GetUserCommand extends Command {
        public GetUserCommand(LogEntity logEntity) {
            this.logEntity = logEntity;
        }

        @Override
        Object execute() {
            return logEntity.getUser();
        }
    }

    private class GetDateCommand extends Command {
        public GetDateCommand(LogEntity logEntity) {
            this.logEntity = logEntity;
        }

        @Override
        Object execute() {
            return logEntity.getDate();
        }
    }

    private class GetEventCommand extends Command {
        public GetEventCommand(LogEntity logEntity) {
            this.logEntity = logEntity;
        }

        @Override
        Object execute() {
            return logEntity.getEvent();
        }
    }

    private class GetStatusCommand extends Command {
        public GetStatusCommand(LogEntity logEntity) {
            this.logEntity = logEntity;
        }

        @Override
        Object execute() {
            return logEntity.getStatus();
        }
    }
}