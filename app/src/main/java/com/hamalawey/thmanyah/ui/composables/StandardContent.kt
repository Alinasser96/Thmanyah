package com.hamalawey.thmanyah.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hamalawey.domain.model.AudioArticle
import com.hamalawey.domain.model.Audiobook
import com.hamalawey.domain.model.Content
import com.hamalawey.domain.model.Episode
import com.hamalawey.domain.model.Podcast
import com.hamalawey.domain.model.Section
import com.hamalawey.domain.model.UnknownContent

@Composable
fun StandardContentRow(content: List<Content>, type: String) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(content) { contentItem ->
            ContentItemSwitcher(
                contentItem,
                type,
                modifier = Modifier.width(350.dp)
            )
        }
    }
}

@Composable
fun SquareContentRow(content: List<Content>, type: String) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(content) { contentItem ->
            ContentItemSwitcher(
                contentItem,
                type,
                modifier = Modifier.width(130.dp)
            )
        }
    }
}

@Composable
fun BigSquareContentRow(content: List<Content>, type: String) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(content) { contentItem ->
            ContentItemSwitcher(
                contentItem,
                type,
                modifier = Modifier.size(250.dp)
            )
        }
    }
}

@Composable
fun TwoLinesContentRows(content: List<Content>, type: String) {
    val chunkedContent = content.chunked(2)

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(chunkedContent) { contentPair ->
            Column(
                modifier = Modifier.width(130.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                contentPair.forEach { contentItem ->
                    ContentItemSwitcher(
                        contentItem,
                        type,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun ContentItemSwitcher(
    contentItem: Content,
    sectionType: String,
    modifier: Modifier = Modifier
) {
    when (contentItem) {
        is Podcast -> PodcastItem(podcast = contentItem, type = sectionType, modifier = modifier)
        is Episode -> EpisodeItem(episode = contentItem, type = sectionType, modifier = modifier)
        is Audiobook -> AudiobookItem(audiobook = contentItem, type = sectionType, modifier = modifier)
        is AudioArticle -> AudioArticleItem(audioArticle = contentItem, type = sectionType, modifier = modifier)
        is UnknownContent -> UnknownItem(unknownContent = contentItem, type = sectionType, modifier = modifier)
    }
}

@Composable
fun PodcastItem(podcast: Podcast, type: String, modifier: Modifier = Modifier) {
    when (type) {
        "queue" -> QueuePodcastItem(podcast, modifier)
        "square" -> SquarePodcastItem(podcast, modifier)
        "big_square", "big square" -> BigSquarePodcastItem(podcast, modifier)
        "2_lines_grid" -> SquarePodcastItem(podcast, modifier)
        else -> SquarePodcastItem(podcast, modifier)
    }
}

@Composable
fun EpisodeItem(episode: Episode, type: String, modifier: Modifier = Modifier) {
    when (type) {
        "queue" -> QueueEpisodeItem(episode, modifier)
        "square" -> SquareEpisodeItem(episode, modifier)
        "big_square", "big square" -> BigSquareEpisodeItem(episode, modifier)
        "2_lines_grid" -> SquareEpisodeItem(episode, modifier)
        else -> SquareEpisodeItem(episode, modifier)
    }
}

@Composable
fun AudiobookItem(audiobook: Audiobook, type: String, modifier: Modifier = Modifier) {
    when (type) {
        "queue" -> QueueAudiobookItem(audiobook, modifier)
        "square" -> SquareAudiobookItem(audiobook, modifier)
        "big_square", "big square" -> BigSquareAudiobookItem(audiobook, modifier)
        "2_lines_grid" -> SquareAudiobookItem(audiobook, modifier)
        else -> SquareAudiobookItem(audiobook, modifier)
    }
}

@Composable
fun AudioArticleItem(audioArticle: AudioArticle, type: String, modifier: Modifier = Modifier) {
    when (type) {
        "queue" -> QueueAudioArticleItem(audioArticle, modifier)
        "square" -> SquareAudioArticleItem(audioArticle, modifier)
        "big_square", "big square" -> BigSquareAudioArticleItem(audioArticle, modifier)
        "2_lines_grid" -> SquareAudioArticleItem(audioArticle, modifier)
        else -> SquareAudioArticleItem(audioArticle, modifier)
    }
}

@Composable
fun UnknownItem(unknownContent: UnknownContent, type: String, modifier: Modifier = Modifier) {
    when (type) {
        "queue" -> QueueUnknownItem(unknownContent, modifier)
        "square" -> SquareUnknownItem(unknownContent, modifier)
        "big_square", "big square" -> BigSquareUnknownItem(unknownContent, modifier)
        "2_lines_grid" -> SquareUnknownItem(unknownContent, modifier)
        else -> SquareUnknownItem(unknownContent, modifier)
    }
}

@Composable
fun SectionContent(section: Section) {
    when (section.type) {
        "queue" -> StandardContentRow(section.content, section.type)
        "square" -> SquareContentRow(section.content, section.type)
        "big_square", "big square" -> BigSquareContentRow(section.content, section.type)
        "2_lines_grid" -> TwoLinesContentRows(section.content, section.type)
        else -> SquareContentRow(section.content, section.type)
    }
}