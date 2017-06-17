using DataAccessLayer.DbContext;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServiceLogicLayer.Models
{
    public class ConcurentModel
    {
        public int SkipCount { get; set; }
        public int TakeCount { get; set; }
        public int StudentId { get; set; }
        public int CourseId { get; set; }
        public int? UniversityId { get; set; }
        public int? DisciplineBranchId { get; set; }
        public int? DisciplineId { get; set; }
        public int? FacultyId { get; set; }
        public StudentRatingType StudentRatingType { get; set; }
    }
}
