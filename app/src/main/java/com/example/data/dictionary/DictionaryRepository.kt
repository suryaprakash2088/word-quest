package com.example.data.dictionary

import com.example.data.model.DictionaryEntry

object DictionaryRepository {

    private val entries: Map<String, DictionaryEntry> = mapOf(
        "SPAN" to DictionaryEntry("SPAN", "noun", "The full extent of something from end to end, or the amount of space that something covers.", "The bridge has an impressive span across the river."),
        "PAN" to DictionaryEntry("PAN", "noun", "A metal container used for cooking food.", "She heated the butter in a small frying pan."),
        "NAP" to DictionaryEntry("NAP", "noun", "A short, light sleep, especially during the day.", "Taking a 20-minute nap restored his energy."),
        "SNAP" to DictionaryEntry("SNAP", "verb", "To break suddenly and completely, typically with a sharp cracking sound.", "A branch snapped under his weight."),
        "ASP" to DictionaryEntry("ASP", "noun", "A small venomous snake of Africa and southern Europe.", "The ancient legend speaks of Cleopatra and the asp."),
        "SPA" to DictionaryEntry("SPA", "noun", "A mineral spring considered to have health-giving properties; a resort with such springs.", "We spent a relaxing weekend at a mountain spa."),
        "STAR" to DictionaryEntry("STAR", "noun", "A luminous astronomical point in the night sky.", "The northern star guided sailors across the dark seas."),
        "RAT" to DictionaryEntry("RAT", "noun", "A rodent resembling a mouse but larger.", "The inquisitive rat scurried across the stone floor."),
        "ART" to DictionaryEntry("ART", "noun", "The expression or application of human creative skill and imagination.", "The museum displays works of modern and classical art."),
        "TAR" to DictionaryEntry("TAR", "noun", "A dark, thick, flammable liquid distilled from wood or coal.", "Fresh tar covered the newly paved country road."),
        "SAT" to DictionaryEntry("SAT", "verb", "Past tense of sit; adopted a sitting position.", "They sat together under the sheltering oak."),
        "RATS" to DictionaryEntry("RATS", "noun", "Plural of rat; also an informal exclamation of annoyance.", "Rats, I forgot to bring the keys!"),
        "LEAF" to DictionaryEntry("LEAF", "noun", "A flattened green outgrowth from the stem of a vascular plant.", "A golden leaf floated down onto the autumn path."),
        "ALE" to DictionaryEntry("ALE", "noun", "A type of beer brewed using a warm fermentation method.", "He ordered a pint of traditional English ale."),
        "FLEA" to DictionaryEntry("FLEA", "noun", "A small wingless parasitic insect that leaps.", "The circus dog was treated for a tiny flea."),
        "TREES" to DictionaryEntry("TREES", "noun", "Woody perennial plants typically having a single stem or trunk.", "Ancient redwood trees towered into the misty sky."),
        "REST" to DictionaryEntry("REST", "verb", "To cease work or movement in order to relax, sleep, or recover strength.", "Let us pause here and rest before continuing."),
        "SEE" to DictionaryEntry("SEE", "verb", "To perceive with the eyes; discern visually.", "From the mountaintop you can see the distant horizon."),
        "STEER" to DictionaryEntry("STEER", "verb", "To guide or control the movement of a vehicle, vessel, or aircraft.", "He used both hands to steer the sailboat safely."),
        "RESET" to DictionaryEntry("RESET", "verb", "To set again or differently, or return to zero.", "Press the red button to reset the timer."),
        "WATER" to DictionaryEntry("WATER", "noun", "A colorless, transparent, odorless liquid forming the seas, lakes, and rivers.", "Fresh mountain water poured from the stone fountain."),
        "TEAR" to DictionaryEntry("TEAR", "noun", "A drop of clear salty liquid secreted by glands in a person's eye.", "A tear of joy ran down her cheek."),
        "RAW" to DictionaryEntry("RAW", "adjective", "Not cooked; in a natural or unrefined state.", "Raw vegetables retain maximum natural crispness."),
        "ATE" to DictionaryEntry("ATE", "verb", "Past tense of eat.", "We ate fresh warm bread straight from the stone oven."),
        "WEAR" to DictionaryEntry("WEAR", "verb", "To have on one's body as clothing, decoration, or protection.", "Be sure to wear warm boots during winter."),
        "OCEAN" to DictionaryEntry("OCEAN", "noun", "A very large expanse of sea, in particular each of the main areas into which the sea is divided.", "The sun dipped beneath the calm azure ocean."),
        "CANOE" to DictionaryEntry("CANOE", "noun", "A narrow, lightweight boat pointed at both ends and propelled by paddles.", "We paddled our cedar canoe down the gentle river."),
        "ONE" to DictionaryEntry("ONE", "number", "The lowest cardinal number; half of two.", "Step one is always the most important step."),
        "CONE" to DictionaryEntry("CONE", "noun", "A solid geometric shape that tapers smoothly from a flat base to a point.", "She ordered two scoops of vanilla in a waffle cone."),
        "ACNE" to DictionaryEntry("ACNE", "noun", "The occurrence of inflamed or infected sebaceous glands in the skin.", "Dermatologists recommend gentle cleansing for acne."),
        "ONCE" to DictionaryEntry("ONCE", "adverb", "On one occasion or for one time only; formerly.", "Once upon a time, there was a hidden emerald forest."),
        "PEAKS" to DictionaryEntry("PEAKS", "noun", "The pointed tops of mountains.", "Snow dusted the majestic jagged peaks."),
        "SPEAK" to DictionaryEntry("SPEAK", "verb", "To say words, convey information, or express feelings.", "She spoke with clarity, passion, and warmth."),
        "APE" to DictionaryEntry("APE", "noun", "A large primate that lacks a tail, including chimps, gorillas, and orangutans.", "The great ape watched curiously from the tree canopy."),
        "SUNLIT" to DictionaryEntry("SUNLIT", "adjective", "Lit by the sun.", "A sunlit clearing appeared in the dense pine forest."),
        "SUIT" to DictionaryEntry("SUIT", "noun", "A set of outer clothes made of the same fabric.", "He wore a crisp navy suit to the gala."),
        "NUT" to DictionaryEntry("NUT", "noun", "A fruit consisting of a hard shell protecting an edible kernel.", "Squirrels gathered acorns and hazel nuts for the cold months."),
        "LIST" to DictionaryEntry("LIST", "noun", "A number of connected items or names written consecutively.", "She checked off every item on her travel list."),
        "PLANET" to DictionaryEntry("PLANET", "noun", "A celestial body moving in an elliptical orbit around a star.", "Earth is our vibrant blue and green home planet."),
        "PLANT" to DictionaryEntry("PLANT", "noun", "A living organism of the kind exemplified by trees, shrubs, herbs, and mosses.", "She placed the potted fern where sunlight filtered gently."),
        "LATE" to DictionaryEntry("LATE", "adjective", "Doing something or taking place after the expected or usual time.", "The twilight arrival was late, yet peaceful."),
        "NET" to DictionaryEntry("NET", "noun", "A fabric made of open mesh used for catching, enclosing, or filtering.", "Fishermen hauled in their woven fishing net."),
        "SILVER" to DictionaryEntry("SILVER", "noun", "A precious shiny grayish-white metallic element.", "The moonlight cast a silver sheen across the lake."),
        "LIVE" to DictionaryEntry("LIVE", "verb", "To remain alive; have one's home in a specified place.", "Songbirds live happily among the cedar branches."),
        "RISE" to DictionaryEntry("RISE", "verb", "To move upward from a lower position.", "Watch the golden sun rise over the ocean."),
        "GARDEN" to DictionaryEntry("GARDEN", "noun", "A piece of ground adjoining a house, used for growing flowers, fruit, or vegetables.", "Lavender and sweet jasmine bloomed in her walled garden."),
        "DANGER" to DictionaryEntry("DANGER", "noun", "The possibility of suffering harm or injury.", "He navigated around the hidden rocks without danger."),
        "READ" to DictionaryEntry("READ", "verb", "To look at and comprehend the meaning of written words.", "She loved to read old classic poetry in quiet afternoons."),
        "CASTLE" to DictionaryEntry("CASTLE", "noun", "A large building fortified against attack with thick walls and towers.", "The medieval castle stood proudly atop the granite cliff."),
        "TALE" to DictionaryEntry("TALE", "noun", "A fictitious or true narrative or story.", "The elder shared a wondrous tale of ancient explorers."),
        "SALE" to DictionaryEntry("SALE", "noun", "The exchange of a commodity for money.", "The merchant celebrated the annual harvest sale."),
        "BRIDGE" to DictionaryEntry("BRIDGE", "noun", "A structure carrying a pathway or road across an obstacle.", "A stone bridge arched gracefully across the rushing stream."),
        "RIDGE" to DictionaryEntry("RIDGE", "noun", "A long narrow hilltop, mountain range, or watershed.", "Hikers traversed the windswept pine ridge at dawn."),
        "BIRD" to DictionaryEntry("BIRD", "noun", "A warm-blooded egg-laying vertebrate distinguished by feathers and wings.", "A bluebird sang its morning melody outside the window."),
        "FLIGHT" to DictionaryEntry("FLIGHT", "noun", "The action or process of flying through the air.", "The swallow's aerial flight was effortless and acrobatic."),
        "LIGHT" to DictionaryEntry("LIGHT", "noun", "The natural agent that stimulates sight and makes things visible.", "Warm golden light filtered through the autumn leaves."),
        "GIFT" to DictionaryEntry("GIFT", "noun", "A thing given willingly to someone without payment; a present.", "The handmade puzzle box was a cherished gift."),
        "WONDER" to DictionaryEntry("WONDER", "noun", "A feeling of surprise mingled with admiration, caused by something beautiful or unexpected.", "She gazed in wonder at the shooting star."),
        "DOWN" to DictionaryEntry("DOWN", "adverb", "Toward or in a lower place or position.", "Raindrops trickled down the smooth glass pane."),
        "WORD" to DictionaryEntry("WORD", "noun", "A single distinct meaningful element of speech or writing.", "A kind word can brighten anyone's journey."),
        "SPRING" to DictionaryEntry("SPRING", "noun", "The season after winter and before summer, in which vegetation begins to appear.", "Spring brought blossoms and birdsong to the valley."),
        "RING" to DictionaryEntry("RING", "noun", "A small circular band, typically of precious metal.", "She wore a golden ring etched with ivy leaves."),
        "SING" to DictionaryEntry("SING", "verb", "To make musical sounds with the voice, especially words with a set tune.", "The choir gathered to sing harmoniously in the cathedral.")
    )

    fun lookup(rawWord: String): DictionaryEntry {
        val word = rawWord.trim().uppercase()
        return entries[word] ?: DictionaryEntry(
            word = word,
            partOfSpeech = "noun / verb",
            definition = "A valid English dictionary word discovered in Word Quest puzzles.",
            example = "Players unlocked \"$word\" to expand their vocabulary score!"
        )
    }

    fun getAllKnownWords(): List<DictionaryEntry> {
        return entries.values.sortedBy { it.word }
    }

    fun searchWords(query: String): List<DictionaryEntry> {
        if (query.isBlank()) return getAllKnownWords()
        val q = query.trim().uppercase()
        return entries.values.filter {
            it.word.contains(q) || it.definition.contains(q, ignoreCase = true)
        }.sortedBy { it.word }
    }
}
