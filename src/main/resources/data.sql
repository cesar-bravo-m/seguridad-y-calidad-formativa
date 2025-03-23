-- Clear existing data
DELETE FROM event;
DELETE FROM profiles;
DELETE FROM users;

-- Insert seed data
INSERT INTO event (category, description, address, time, organizers, image, available_services, attractions) VALUES
('Torneo', 'Torneo Nacional de League of Legends', 'Centro de Convenciones Santiago', '2024-05-15 10:00', 'ESL Gaming Chile', 'https://placehold.co/600x400/9D4EDD/ffffff?text=LoL+Tournament', 'Food Court, WiFi, Parking', 'Prize Pool $5000, Meet Pro Players, Cosplay Contest'),

('Junta', 'Minecraft Building Championship', 'Teatro Caupolicán', '2024-06-01 14:00', 'Minecraft Chile Community', 'https://placehold.co/600x400/7B2CBF/ffffff?text=Minecraft+Event', 'Snack Bar, Free Water Stations', 'Dream, Creative Workshops, YouTuber Meet & Greet'),

('Charla', 'Pokemon Trading Card Tournament', 'Mall Plaza Vespucio', '2024-05-20 11:00', 'Pokemon Fans Chile', 'https://placehold.co/600x400/5A189A/ffffff?text=Pokemon+TCG', 'Card Shop, Trading Area', 'Official Tournament, Card Trading, Rare Card Exhibition'),

('Torneo', 'Valorant Regional Finals', 'Movistar Arena', '2024-07-10 16:00', 'Riot Games LATAM', 'https://placehold.co/600x400/3C096C/ffffff?text=Valorant+Finals', 'VIP Lounge, Food Court, Merchandise Store', 'Pro Teams Competition, Meet & Greet, Exclusive Merch'),

('Torneo', 'Super Smash Bros Ultimate Tournament', 'Club Chocolate', '2024-05-25 15:00', 'FGC Chile', 'https://placehold.co/600x400/240046/ffffff?text=Smash+Tournament', 'Gaming Stations, Snack Bar', '1v1 Tournament, Free Play Area, Cash Prizes'),

('Exposición', 'Gaming Tech Expo 2024', 'Espacio Riesco', '2024-08-01 09:00', 'Tech Gaming Chile', 'https://placehold.co/600x400/10002B/ffffff?text=Tech+Expo', 'Food Court, WiFi, Parking, Rest Areas', 'Hardware Demos, VR Experience, Gaming PC Showcase'),

('Torneo', 'Fortnite Summer Cup', 'Estadio Nacional', '2024-06-15 12:00', 'Epic Games LATAM', 'https://placehold.co/600x400/9D4EDD/ffffff?text=Fortnite+Cup', 'Food Trucks, Gaming Stations', 'Solo Tournament, Dance Competition, Cosplay Contest'),

('Junta', 'Retro Gaming Festival', 'Centro Cultural CorpArtes', '2024-07-20 10:00', 'Retro Gamers Chile', 'https://placehold.co/600x400/7B2CBF/ffffff?text=Retro+Gaming', 'Arcade Machines, Repair Workshop', 'Classic Consoles, Trading Area, Tournaments'),

('Charla', 'FIFA 24 National Championship', 'Teatro Teletón', '2024-05-30 13:00', 'EA Sports Chile', 'https://placehold.co/600x400/5A189A/ffffff?text=FIFA+Championship', 'Gaming Stations, Sports Bar', 'Pro Tournament, Amateur Division, Meet Soccer Stars'),

('Exposición', 'Anime & Gaming Convention', 'Casa Piedra', '2024-09-01 10:00', 'Anime Chile', 'https://placehold.co/600x400/3C096C/ffffff?text=Gaming+Convention', 'Food Court, Cosplay Area, Artist Alley', 'Gaming Tournaments, Cosplay Contest, Artist Market'),

('Junta', 'Call of Duty Warzone Tournament', 'Teatro Coliseo', '2024-06-10 14:00', 'Activision LATAM', 'https://placehold.co/600x400/240046/ffffff?text=COD+Tournament', 'Gaming Area, Snack Bar', 'Squad Tournament, Free Play, Pro Player Exhibition'),

('Charla', 'Game Developers Summit', 'Hotel W Santiago', '2024-08-15 09:00', 'Game Dev Chile', 'https://placehold.co/600x400/10002B/ffffff?text=Dev+Summit', 'Conference Rooms, Networking Area', 'Workshops, Indie Game Showcase, Industry Talks'),

('Torneo', 'Mario Kart Championship', 'Portal La Dehesa', '2024-07-01 11:00', 'Nintendo Fans Chile', 'https://placehold.co/600x400/9D4EDD/ffffff?text=Mario+Kart', 'Gaming Stations, Kids Area', 'Tournament, Free Play, Nintendo Switch Demo'),

('Torneo', 'Among Us Live Event', 'Parque Araucano', '2024-06-20 16:00', 'Gaming Social Club', 'https://placehold.co/600x400/7B2CBF/ffffff?text=Among+Us', 'Food Court, Theme Areas', 'Live Action Game, Cosplay, Team Building'),

('Torneo', 'Rocket League Tournament', 'Teatro Nescafé', '2024-07-25 15:00', 'Psyonix LATAM', 'https://placehold.co/600x400/5A189A/ffffff?text=Rocket+League', 'Gaming Area, Sports Bar', '3v3 Tournament, Freestyle Competition'),

('Exposición', 'Gaming Careers Fair', 'Universidad de Chile', '2024-08-20 10:00', 'Gaming Industry Chile', 'https://placehold.co/600x400/3C096C/ffffff?text=Career+Fair', 'Conference Rooms, Interview Booths', 'Industry Panels, Job Fair, Portfolio Reviews'),

('Torneo', 'Hearthstone Tournament', 'Club Providencia', '2024-06-05 12:00', 'Blizzard LATAM', 'https://placehold.co/600x400/240046/ffffff?text=Hearthstone', 'Card Gaming Area, Cafeteria', 'Card Tournament, Deck Building Workshop'),

('Junta', 'VR Gaming Experience', 'Mall Sport', '2024-07-15 11:00', 'VR Chile', 'https://placehold.co/600x400/10002B/ffffff?text=VR+Gaming', 'VR Stations, Rest Area', 'VR Demos, Competitions, New Tech Showcase'),

('Torneo', 'Indie Games Festival', 'GAM Cultural Center', '2024-09-10 10:00', 'Indie Dev Chile', 'https://placehold.co/600x400/9D4EDD/ffffff?text=Indie+Games', 'Demo Stations, Meeting Rooms', 'Game Demos, Developer Talks, Networking'),

('Charla', 'E-Sports Awards 2024', 'Hotel Sheraton', '2024-10-01 19:00', 'E-Sports Chile', 'https://placehold.co/600x400/7B2CBF/ffffff?text=Esports+Awards', 'VIP Lounge, Gala Dinner', 'Awards Ceremony, Pro Player Recognition, Networking'); 