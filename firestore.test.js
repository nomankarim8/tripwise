const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

// --- SECURITY BOUNDS TESTS ---

test("Unauthenticated user: cannot read trips", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("trips").get());
});

test("Unauthenticated user: cannot create user profile", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      name: "Alice",
      email: "alice@example.com",
    })
  );
});

test("Authenticated user: can create & read their own profile", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      name: "Alice Traveler",
      email: "alice@example.com",
      phone: "+8801700000000",
      bio: "Travel lover",
    })
  );
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).get());
});

test("Authenticated user: cannot read or update another user's profile", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection("users").doc(BOB_UID).set({
      userId: BOB_UID,
      name: "Bob",
      email: "bob@example.com",
    });
  });

  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(aliceDb.collection("users").doc(BOB_UID).get());
  await assertFails(
    aliceDb.collection("users").doc(BOB_UID).update({
      name: "Hacked",
    })
  );
});

test("Authenticated user: can create and read their own trip", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(
    aliceDb.collection("trips").doc("trip_1").set({
      id: "trip_1",
      userId: ALICE_UID,
      from: "Dhaka",
      to: "Chittagong",
      distanceKm: 245.0,
      passengers: 3,
      vehicleType: "Sedan Car",
      fuelCost: 2200.0,
      tollCost: 450.0,
      parkingCost: 100.0,
      foodOthers: 500.0,
      totalCost: 3250.0,
    })
  );
  await assertSucceeds(aliceDb.collection("trips").doc("trip_1").get());
});

test("Authenticated user: cannot read another user's trip", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection("trips").doc("bob_trip").set({
      id: "bob_trip",
      userId: BOB_UID,
      from: "Dhaka",
      to: "Sylhet",
      distanceKm: 240.0,
      passengers: 2,
      vehicleType: "Motorcycle",
      fuelCost: 800.0,
      tollCost: 150.0,
      parkingCost: 50.0,
      foodOthers: 200.0,
      totalCost: 1200.0,
    });
  });

  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(aliceDb.collection("trips").doc("bob_trip").get());
});

test("Authenticated user: can query trips filtered by their userId", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection("trips").doc("alice_trip").set({
      id: "alice_trip",
      userId: ALICE_UID,
      from: "Dhaka",
      to: "Cox's Bazar",
      distanceKm: 390.0,
      passengers: 4,
      vehicleType: "Sedan Car",
      fuelCost: 3500.0,
      tollCost: 650.0,
      parkingCost: 200.0,
      foodOthers: 1000.0,
      totalCost: 5350.0,
    });
  });

  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(
    aliceDb.collection("trips").where("userId", "==", ALICE_UID).get()
  );
});

test("Authenticated user: cannot query trips without userId filter", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(aliceDb.collection("trips").get());
});
