package LABS.LAB5;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.*;
import java.util.TreeSet;

class NoSuchRoomException extends Exception {
    public NoSuchRoomException(String message) {
        super(message);
    }
}

class NoSuchUserException extends Exception {
    public NoSuchUserException(String message) {
        super(message);
    }
}

class ChatRoom {
    private String name;
    private Set<String> users;

    public ChatRoom(String name) {
        this.name = name;
        this.users = new TreeSet<>();
    }

    public void addUser(String username) {
        this.users.add(username);
    }

    public void removeUser(String username) {
        this.users.removeIf(u -> u.equals(username));
    }

    public boolean hasUser(String username) {
        return this.users.contains(username);
    }

    public int numUsers() {
        return this.users.size();
    }

    @Override
    public String toString() {
        if (this.users.isEmpty()) {
            return this.name + "\n" + "EMPTY\n";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(this.name + "\n");
            for (String user : this.users) {
                sb.append(user + "\n");
            }
            return sb.toString();
        }
    }

    public String getName() {
        return name;
    }
}


class ChatSystem {
    private Map<String, ChatRoom> rooms;
    private Set<String> users;

    public ChatSystem() {
        this.rooms = new TreeMap<>();
        this.users = new TreeSet<>();
    }

    public void addRoom(String roomName) {
        rooms.putIfAbsent(roomName, new ChatRoom(roomName));
    }

    public void removeRoom(String roomName) {
        rooms.remove(roomName);
    }

    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        ChatRoom cr = rooms.get(roomName);
        if (cr == null) {
            throw new NoSuchRoomException(roomName);
        } else {
            return cr;
        }
    }

    public void register(String userName) {
        if (rooms.isEmpty()) return;
        this.users.add(userName);
        rooms.values()
                .stream()
                .min(Comparator.comparingInt(ChatRoom::numUsers)
                        .thenComparing(ChatRoom::getName))
                .get()
                .addUser(userName);
    }

    public void registerAndJoin(String userName, String roomName) throws NoSuchRoomException {
        this.users.add(userName);
        ChatRoom cr = rooms.get(roomName);
        if (cr == null) {
            throw new NoSuchRoomException(roomName);
        }
        this.rooms.get(roomName).addUser(userName);
    }

    public void joinRoom(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        ChatRoom cr = rooms.get(roomName);
        if (cr == null) {
            throw new NoSuchRoomException(roomName);
        } else {
            cr.addUser(userName);
        }
    }

    public void leaveRoom(String username, String roomName) throws NoSuchRoomException, NoSuchUserException {
        ChatRoom cr = rooms.get(roomName);
        if (cr == null) {
            throw new NoSuchRoomException(roomName);
        } else {
            cr.removeUser(username);
        }
    }

    public void followFriend(String username, String friend_username) {
        rooms.values()
                .stream()
                .filter(cr -> cr.hasUser(friend_username))
                .forEach(cr -> cr.addUser(username));
    }
}

@SuppressWarnings("unused")
public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr.addUser(jin.next());
                if (k == 1) cr.removeUser(jin.next());
                if (k == 2) System.out.println(cr.hasUser(jin.next()));
            }

            System.out.println(cr.toString());
            n = jin.nextInt();
            if (n == 0) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr2.addUser(jin.next());
                if (k == 1) cr2.removeUser(jin.next());
                if (k == 2) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if (k == 1) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while (true) {
                String cmd = jin.next();
                if (cmd.equals("stop")) break;
                if (cmd.equals("print")) {
                    System.out.println(cs.getRoom(jin.next()) + "\n");
                    continue;
                }
                for (Method m : mts) {
                    if (m.getName().equals(cmd)) {
                        String params[] = new String[m.getParameterTypes().length];
                        for (int i = 0; i < params.length; ++i) params[i] = jin.next();
                        m.invoke(cs, (Object[]) params);
                    }
                }
            }
        }
    }
}