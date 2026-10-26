const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_farmer";
const BOB_UID = "bob_farmer";

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

test("Unauthenticated user: cannot read fields", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("fields").get());
});

test("Authenticated user: cannot read another user's fields", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection("users").doc(BOB_UID).collection("fields").doc("field_1").set({
      id: "field_1",
      userId: BOB_UID,
      name: "Bob's Field",
      cropType: "Wheat",
      acreage: 5.0,
      createdAt: new Date(),
      updatedAt: new Date(),
    });
  });

  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(aliceDb.collection("users").doc(BOB_UID).collection("fields").doc("field_1").get());
  await assertFails(aliceDb.collection("users").doc(BOB_UID).collection("fields").get());
});

test("Authenticated user: can create, get and list own field", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const fieldRef = aliceDb.collection("users").doc(ALICE_UID).collection("fields").doc("field_alice_1");

  await assertSucceeds(
    fieldRef.set({
      id: "field_alice_1",
      userId: ALICE_UID,
      name: "North Orchard",
      cropType: "Apples",
      acreage: 3.5,
      createdAt: new Date(),
      updatedAt: new Date(),
    })
  );

  await assertSucceeds(fieldRef.get());
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).collection("fields").get());
});

test("Authenticated user: cannot create field with mismatched userId", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const fieldRef = aliceDb.collection("users").doc(ALICE_UID).collection("fields").doc("field_fake");

  await assertFails(
    fieldRef.set({
      id: "field_fake",
      userId: BOB_UID, // Spoofed ID
      name: "Malicious Field",
      cropType: "Corn",
      acreage: 2.0,
      createdAt: new Date(),
      updatedAt: new Date(),
    })
  );
});

test("Authenticated user: can create growth milestone and crop scan", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();

  const msRef = aliceDb.collection("users").doc(ALICE_UID).collection("growth_milestones").doc("ms_1");
  await assertSucceeds(
    msRef.set({
      id: "ms_1",
      userId: ALICE_UID,
      fieldId: "field_alice_1",
      stage: "Vegetative V2",
      plantHeightCm: 15,
      healthScore: 9,
      notes: "Healthy sprouts",
      dateLabel: "Day 14",
      daysAfterSowing: 14,
      photoPath: "/local/photo.jpg",
      createdAt: new Date(),
      updatedAt: new Date(),
    })
  );

  const scanRef = aliceDb.collection("users").doc(ALICE_UID).collection("scans").doc("scan_1");
  await assertSucceeds(
    scanRef.set({
      id: "scan_1",
      userId: ALICE_UID,
      cropName: "Tomato",
      diseaseName: "Early Blight",
      severity: "MODERATE",
      confidence: 0.94,
      affectedAreaPercent: 12.5,
      notes: "Yellow lesions",
      createdAt: new Date(),
      updatedAt: new Date(),
    })
  );
});
