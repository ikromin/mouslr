#!/bin/bash
#
# Mouslr - Mouse Tracking Pixel Art!
# http://mouslr.kr0m.in
# Copyright (C) 2015  Igor Kromin
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; version 2 of the License.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License along
# with this program; if not, write to the Free Software Foundation, Inc.,
# 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
#
# You can find this and my other open source projects here - http://github.com/ikromin
#

javapackager -deploy -width 517 -height 270 -title "Mouslr" -appclass net.igorkromin.mouslr.Capture -native dmg -name "Mouslr" -outdir ~/Downloads -outfile Mouslr -srcdir "/Volumes/Data HD/Personal/MouseCaptureArt/out/artifacts/Mouslr_jar"