package com.edbrn.Campfires.files;

import static org.junit.jupiter.api.Assertions.*;

import com.edbrn.Campfires.files.exceptions.CampfireLimitReachedException;
import com.edbrn.Campfires.files.jsonmodel.Campfire;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CampfiresConfigTest {
  private final String configFile = "campfires-test.json";

  @AfterEach
  @BeforeEach
  public void afterEach() {
    File configFile = new File(this.configFile);
    configFile.delete();
  }

  @Test
  public void testCreatesCampfiresConfigIfItDoesntExistAlready() {
    File configFile = new File(this.configFile);
    assertFalse(configFile.exists());

    CampfiresConfig config = new CampfiresConfig(Logger.getAnonymousLogger(), this.configFile);
    config.getCampfires();

    assertTrue(configFile.exists());
  }

  @Test
  public void testPopulatesFileWithSingleKeyEmptyArray() {
    File configFile = new File(this.configFile);
    assertFalse(configFile.exists());

    CampfiresConfig config = new CampfiresConfig(Logger.getAnonymousLogger(), this.configFile);
    config.getCampfires();

    try {
      String configContent = Files.readString(Path.of(this.configFile));
      assertEquals(configContent, "{\"campfires\": {}}");
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
  public void testAddCampfireWhenPlayerHasNoCampfires() {
    CampfiresConfig config = new CampfiresConfig(Logger.getAnonymousLogger(), this.configFile);
    config.getCampfires();

    Player player = Mockito.mock(Player.class);
    UUID uuid = UUID.randomUUID();
    Mockito.when(player.getUniqueId()).thenReturn(uuid);

    try {
      config.addCampfire(1, 2, 3, player);
    } catch (CampfireLimitReachedException ignored) {
    }

    try {
      String configContent = Files.readString(Path.of(this.configFile));
      assertEquals(
          String.format("{\"campfires\":{\"%s\":[{\"x\":1,\"y\":2,\"z\":3}]}}", uuid.toString()),
          configContent);
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
  public void testAddMultipleCampfires() {
    CampfiresConfig config = new CampfiresConfig(Logger.getAnonymousLogger(), this.configFile);
    config.getCampfires();

    Player player = Mockito.mock(Player.class);
    UUID uuid = UUID.randomUUID();
    Mockito.when(player.getUniqueId()).thenReturn(uuid);

    try {
      config.addCampfire(1, 2, 3, player);
      config.addCampfire(222, 333, 444, player);
    } catch (CampfireLimitReachedException ignored) {
    }

    try {
      String configContent = Files.readString(Path.of(this.configFile));
      assertEquals(
          String.format(
              "{\"campfires\":{\"%s\":[{\"x\":1,\"y\":2,\"z\":3},{\"x\":222,\"y\":333,\"z\":444}]}}",
              uuid.toString()),
          configContent);
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
  public void testRemoveCampfire() {
    CampfiresConfig config = new CampfiresConfig(Logger.getAnonymousLogger(), this.configFile);
    config.getCampfires();

    Player player = Mockito.mock(Player.class);
    UUID uuid = UUID.randomUUID();
    Mockito.when(player.getUniqueId()).thenReturn(uuid);

    try {
      config.addCampfire(1, 2, 3, player);
      Campfire campfireToRemove = config.addCampfire(222, 333, 444, player);

      config.removeCampfire(player, campfireToRemove);
    } catch (CampfireLimitReachedException ignored) {
    }

    try {
      String configContent = Files.readString(Path.of(this.configFile));
      assertEquals(
          String.format("{\"campfires\":{\"%s\":[{\"x\":1,\"y\":2,\"z\":3}]}}", uuid.toString()),
          configContent);
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
  public void testAddMultipleCampfiresDifferentPlayers() {
    CampfiresConfig config = new CampfiresConfig(Logger.getAnonymousLogger(), this.configFile);
    config.getCampfires();

    Player player1 = Mockito.mock(Player.class);
    UUID uuid1 = UUID.randomUUID();
    Mockito.when(player1.getUniqueId()).thenReturn(uuid1);

    Player player2 = Mockito.mock(Player.class);
    UUID uuid2 = UUID.randomUUID();
    Mockito.when(player2.getUniqueId()).thenReturn(uuid2);

    try {
      config.addCampfire(1, 2, 3, player1);
      config.addCampfire(222, 333, 444, player2);
      config.addCampfire(555, 212, 119, player2);
    } catch (CampfireLimitReachedException ignored) {
    }

    try {
      String configContent = Files.readString(Path.of(this.configFile));
      assertEquals(
          String.format(
              "{\"campfires\":{\"%s\":[{\"x\":1,\"y\":2,\"z\":3}],\"%s\":[{\"x\":222,\"y\":333,\"z\":444},{\"x\":555,\"y\":212,\"z\":119}]}}",
              uuid1.toString(), uuid2.toString()),
          configContent);
    } catch (Exception e) {
      fail(e);
    }
  }
}
