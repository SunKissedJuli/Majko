package com.coolgirl.majko.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.coolgirl.majko.R
import com.coolgirl.majko.commons.Constantas
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse
import com.coolgirl.majko.navigation.Screen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProjectCard(
    navHostController: NavHostController,
    priorityColor: Color = MaterialTheme.colorScheme.background,
    projectData: ProjectDataResponse,
    onLongTap: (String) -> Unit = {},
    onLongTapRelease: (String) -> Unit = {},
    isSelected: Boolean = false
) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .padding(start = 10.dp, top = 10.dp, end = 10.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(
                3.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            )
            .background(color = priorityColor)
            .combinedClickable(
                onClick = { navHostController.navigate(Screen.ProjectEditor.createRoute(projectData.id)) },
                onLongClick = {
                    if (isSelected) {
                        onLongTapRelease(projectData.id)
                    } else {
                        onLongTap(projectData.id)
                    }
                },
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            Modifier
                .padding(start = 15.dp, top = 10.dp, end = 10.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.27f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(!projectData.author.image.isNullOrEmpty()){
                Image(painter = rememberImagePainter(Constantas.BASE_URI + projectData.author.image),
                    contentDescription = "",
                    Modifier
                        .size(25.dp)
                        .clip(CircleShape))
            }else{
                Box(
                    Modifier
                        .fillMaxHeight(0.8f)
                        .size(25.dp)
                        .aspectRatio(1f)
                        .background(MaterialTheme.colorScheme.primary, shape = CircleShape))
            }
            Spacer(Modifier.width(15.dp))
            Text(
                text = projectData.name,
                modifier = Modifier.fillMaxWidth(0.7f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                softWrap = true,
                maxLines = 2
            )
        }
        Row(
            Modifier
                .padding(start = 15.dp, top = 10.dp, end = 10.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.7f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = projectData.description,
                fontSize = 13.sp,
                fontWeight = FontWeight.Light,
                softWrap = true,
                maxLines = 4
            )
        }
        Row(
            Modifier
                .fillMaxSize()
                .padding(end = 15.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            if (!projectData.isPersonal) {
                Image(painter = painterResource(R.drawable.icon_members), contentDescription = "")
                Spacer(modifier = Modifier.width(3.dp))
                Text(text = projectData.members.size.toString())
            }
        }
    }
}


data class ProjectCardUiState(
    var borderColor: Int = R.color.gray,
)