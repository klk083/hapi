# noinspection SqlNoDataSourceInspectionForFile
INSERT INTO employee VALUES(DEFAULT, 1, "admin", "admin", "$6$jv450qggfl621$nFLSFodaU3qTvW51TGSIebgwqPKEnl5UeYN9J2FQfcRPmSxgAcPXPdUS9f7WaZGGgnbQPPwtwTf2mrSYbYGhh0", "$6$jv450qggfl621");
INSERT INTO employee VALUES(DEFAULT, 4, "Ivar", "ivar", "$6$inrd2vt1sadn9$KGRzE/G2JZf6dXvOFvvTYDiro/avbSPPL.aZWaIjAFFaHCZR7ZZZU.0hT6TJjUztp0U0ARuGiiyDeb5Vw75A.1", "$6$inrd2vt1sadn9");
INSERT INTO employee VALUES(DEFAULT, 4, "Luft lars", "luft", "$6$21ne21aunh713$rImoLoSGxio7xV.BbLP.O7Vx3Ps6TaeTIc0Qcr6OfhMtLIN02X5Qo7xL6U2RRxdTgEc//dVDGcznCmS4390Zn0", "$6$21ne21aunh713");

INSERT INTO role VALUES(DEFAULT, 'CEO');
INSERT INTO role VALUES(DEFAULT, 'Secretary');
INSERT INTO role VALUES(DEFAULT, 'Cook');
INSERT INTO role VALUES(DEFAULT, 'Driver');
INSERT INTO role VALUES(DEFAULT, 'Sales Representative');
INSERT INTO role VALUES(DEFAULT, 'Nutrition Expert');

INSERT INTO customer VALUES(DEFAULT, 'Dummybruker', 'Dummyveien 1', '00000000', 0, false);
INSERT INTO customer VALUES(DEFAULT, 'Ronny', 'Bukta 12', '95408740', 0, false);
INSERT INTO customer VALUES(DEFAULT, 'Ali', 'Gatehjørnet 20041', '92915412', 0, false);
INSERT INTO customer VALUES(DEFAULT, 'Stig', 'Sveinveien 72', '90608050', 0, false);

INSERT INTO menu VALUES(DEFAULT, 'Dummymat', 1, 'Dummy');
INSERT INTO menu VALUES(DEFAULT, 'Sviskeburger', 65, 'Himmelsk burger med smak');
INSERT INTO menu VALUES(DEFAULT, 'Reker', 200, 'Byens beste reker');
INSERT INTO menu VALUES(DEFAULT, 'Kjøttburger uten kjøtt', 300, 'Utrolig bra kjøtt');
INSERT INTO menu VALUES(DEFAULT, 'Pølse i luft', 55, 'Luftig pølse');
INSERT INTO menu VALUES(DEFAULT, 'Guds mat', 35, 'Smaken av gud');
INSERT INTO menu VALUES(DEFAULT, 'Burger Burger', 250, 'Burger inni en burger');
INSERT INTO menu VALUES(DEFAULT, 'Tunnelmat', 76, 'Bra hvis du skal på tur med toget');
INSERT INTO menu VALUES(DEFAULT, 'Baconburger med bacon', 125, 'Meget oppgående mengde bacon');
INSERT INTO menu VALUES(DEFAULT, 'Fritert hamster', 100, 'Myk og fyldig smak');
INSERT INTO menu VALUES(DEFAULT, 'Hakkemat', 500, 'Mat for de som er litt ekstra fan av hakking');

INSERT INTO subscription VALUES(DEFAULT, 'Burger Abonnement', 100, 'For den som vil automatisk få levert burger');

INSERT INTO ingredient VALUES(DEFAULT, 'Burger', 'stk', 100);
INSERT INTO ingredient VALUES(DEFAULT, 'Ost', 'g', 1);
INSERT INTO ingredient VALUES(DEFAULT, 'Luft', 'cm3', 5);
INSERT INTO ingredient VALUES(DEFAULT, 'Potet', 'stk', 100);
INSERT INTO ingredient VALUES(DEFAULT, 'Gress', 'cm3', 0);
INSERT INTO ingredient VALUES(DEFAULT, 'Blod', 'ml', 1000);

INSERT INTO menu_ingredient VALUES(1, 6, 2);
INSERT INTO menu_ingredient VALUES(2, 5, 2);
INSERT INTO menu_ingredient VALUES(3, 5, 200);
INSERT INTO menu_ingredient VALUES(3, 7, 1000);

INSERT INTO orders VALUES (DEFAULT, 2, '2008-11-11 12:50:00', false, false);
INSERT INTO orders VALUES (DEFAULT, 2, '2012-11-11 12:50:00', false, false);
INSERT INTO orders VALUES (DEFAULT, 2, '2012-11-11 12:50:00', true, false);

INSERT INTO menu_order VALUES(1, 1, 1, '', false);

INSERT INTO order_chauffeur VALUES(1, 2);

INSERT INTO order_cook VALUES(2, 4);

INSERT INTO subscription_menu VALUES(1, 1, 1);

INSERT INTO sub_delivery_days VALUES(1, true, true, true, true, true, true, true);

INSERT INTO subscription_customer VALUES(1, 1, 4444-44-44, 4444-44-44);