INSERT INTO app_users (id, password, role, username, location) VALUES (1, 'miaou', 'user', 'arnono', 'SRID=4326;POINT(-118.4079 33.9434)'::geometry);
INSERT INTO cardreference (id, riftboundid, name, set, imageurl, isnew) VALUES (1, 'unl-229*-219', 'Vi - Piltover Enforcer (Signature)', 'UNL', 'https://cmsassets.rgpub.io/sanity/images/dsfx7636/game_data_live/0febae9c611339d9ed65c7ebe43237b5ec42c9a3-744x1039.png?accountingTag=RB', false);
INSERT INTO card (id, quality, language, isborrowed, owner_id, cardreference_id) VALUES (1, 0, 0, false, 1, 1);
INSERT INTO card (id, quality, language, isborrowed, owner_id, cardreference_id) VALUES (2, 0, 0, false, 1, 1);
alter sequence app_users_seq restart with 2;
alter sequence cardreference_seq restart with 2;
alter sequence card_seq restart with 3;
