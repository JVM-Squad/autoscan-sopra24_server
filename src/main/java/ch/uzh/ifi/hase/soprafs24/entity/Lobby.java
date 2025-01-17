package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.exceptions.LobbyLockedException;
import ch.uzh.ifi.hase.soprafs24.exceptions.UnauthorizedException;

import java.util.*;

public class Lobby {

    private String id;
    private final HashMap<String, Player> players;
    private Player host;
    private GameSettings settings;
    private Boolean isGameRunning;
    private Boolean isLobbyFull;

    public Lobby(Player host) {
        // initialize fields
        this.id = UUID.randomUUID().toString();
        this.host = host;
        this.settings = new GameSettings();
        this.isGameRunning = false;
        this.isLobbyFull = false;
        // initialize player list and add host directly
        this.players = new HashMap<String, Player>();
        this.players.put(host.getToken(), host);
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(this.players.values());
    }

    public void addPlayer(Player player) throws LobbyLockedException {
        // if lobby is not full - add player
        if (!isLobbyFull && !isGameRunning) {
            this.players.put(player.getToken(), player);
            // if this player took the last spot, set lobby full
            if (this.players.size() >= this.settings.getMaxPlayers()) {
                this.isLobbyFull = true;
            }
        }
        // if lobby is full or running - throw exception
        else {
            throw new LobbyLockedException("Sorry, the lobby you are trying to join is full.");
        }
    }

    public Player removePlayer(String token) {
        return this.players.remove(token);
    }

    public void kickPlayer(String hostToken, String usernameToKick) throws UnauthorizedException {
        String keyToRemove = "";
        if (this.host.getToken().equals(hostToken)) {
            for (Map.Entry<String, Player> entry : players.entrySet()) {
                if (entry.getValue().getUsername().equals(usernameToKick)) {
                    keyToRemove = entry.getKey();
                }
            }
            players.remove(keyToRemove);
        }
        else {
            throw new UnauthorizedException("You tried to kick a player, but you are not the host of the lobby.");
        }
    }

    public Player getHost() {
        return host;
    }

    public void setHost(Player host) {
        this.host = host;
    }

    public GameSettings getSettings() {
        return settings;
    }

    public void setSettings(GameSettings settings) {
        if (players.size() >= settings.getMaxPlayers()) {
            this.isLobbyFull = true;
        }
        else {
            this.isLobbyFull = false;
        }
        this.settings = settings;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
